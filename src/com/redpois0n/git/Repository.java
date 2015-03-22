package com.redpois0n.git;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import com.redpois0n.git.Change.Type;
import com.redpois0n.gitj.Main;

public class Repository {

	private File folder;
	private List<Commit> commits;
	private List<Tag> tags;

	public Repository(File folder) throws InvalidRepositoryException {
		this.folder = folder;
		
		if (!isValidRepo()) {
			throw new InvalidRepositoryException();
		}
	}

	/**
	 * Returns all commits from this repository
	 * 
	 * @return Commits from cache
	 * @throws Exception
	 */
	public List<Commit> getCommits() throws Exception {
		return getCommits(false);
	}

	/**
	 * Returns all commits from this repository
	 * 
	 * @param update
	 *            If git log should be executed and read
	 * @return Commits either freshly loaded or from cache
	 * @throws Exception
	 */
	public List<Commit> getCommits(boolean update) throws Exception {
		if (!update && commits != null) {
			return commits;
		} else {
			if (commits == null) {
				commits = new ArrayList<Commit>();
			} else {
				commits.clear();
			}

			List<Tag> tags = getTags();
			List<String> raw = run(new String[] { "git", "log", "--pretty=format:Commit;%H;%an;%ae;%ar;%s" });
			Enumeration<String> e = Collections.enumeration(raw);

			String s = e.nextElement();

			while (e.hasMoreElements()) {
				//Main.print("Raw commit data: " + s);
				
				String[] split = s.replace("Commit;", "").split(";");
				String hash = split[0];
				
				Commit c = new Commit(this, split);
				
				for (Tag tag : tags) {
					if (tag.getHash().equals(hash)) {
						c.addTag(tag);
					}
				}

				commits.add(c);
				
				s = e.nextElement();
			}
		}
		
		return commits;
	}
	
	public List<Diff> getUncommitedDiffs() {
		List<Diff> diffs = new ArrayList<Diff>();
		
		try {
			List<String> raw = run(new String[] { "git", "diff", "HEAD" } );
			Enumeration<String> e = Collections.enumeration(raw);

			while (e.hasMoreElements()) {
				String s = e.nextElement();

				while (s.startsWith("diff --git")) {
					Main.print("Diff: " + s);

					String sdiff = s.substring(s.lastIndexOf(" b/") + 3, s.length()).trim();

					s = e.nextElement();
					e.nextElement();
					e.nextElement();

					Diff diff = new Diff(new Commit(this), new File(folder, sdiff), Diff.Type.EDITED);
					diffs.add(diff);

					Chunk current = null;
					
					if (!e.hasMoreElements()) {
						diff.setDataType(Diff.DataType.BINARY);
						break;
					}

					while (!(s = e.nextElement()).startsWith("diff --git")) {
						if (s.startsWith("Binary files ")) {
							diff.setDataType(Diff.DataType.BINARY);						
							if (e.hasMoreElements()) {
								s = e.nextElement();
							}
							break;
						} else if (s.startsWith("Commit;") || !e.hasMoreElements()) {
							break;
						} else if (s.startsWith("@@ ")) {
							String chunk = s.substring(0, s.indexOf("@@", 3) + 2).trim();

							Main.print("Chunk: " + chunk);

							current = new Chunk(diff, chunk);
							diff.addChunk(current);
							continue;
						}
						
						if (current != null) {
							current.addRawLine(s);
							Main.print("Code: " + s);
						} else {
							Main.print("!Code: " + s);
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return diffs;
	}

	public List<Diff> getDiffs(Commit c) {
		List<Diff> diffs = new ArrayList<Diff>();

		try {
			List<String> raw = run(new String[] { "git", "show", "--pretty=format:Commit;%H;%an;%ae;%ar;%s", "--stat", "-p", c.getHash() });
			Enumeration<String> e = Collections.enumeration(raw);

			diffs.clear();

			String s = e.nextElement();

			while (e.hasMoreElements()) {
				while ((s = e.nextElement()).contains("|")) {

				}

				String changes = s;
				Main.print("Changes: " + changes);

				s = e.nextElement();

				while (s.startsWith("diff --git")) {
					Main.print("Diff: " + s);

					String sdiff = s.substring(s.lastIndexOf(" b/") + 3, s.length()).trim();

					s = e.nextElement();

					Diff.Type type;

					if (s.startsWith("deleted")) {
						type = Diff.Type.DELETED;
					} else if (s.startsWith("new")) {
						type = Diff.Type.NEW;
					} else {
						type = Diff.Type.EDITED;
					}

					Diff diff = new Diff(c, new File(folder, sdiff), type);
					diffs.add(diff);

					Chunk current = null;

					while (!(s = e.nextElement()).startsWith("diff --git")) {
						if (s.startsWith("Binary files ")) {
							diff.setDataType(Diff.DataType.BINARY);						
							if (e.hasMoreElements()) {
								s = e.nextElement();
							}
							break;
						} else if (s.startsWith("Commit;") || !e.hasMoreElements()) {
							break;
						} else if (s.startsWith("@@ ")) {
							String chunk = s.substring(0, s.indexOf("@@", 3) + 2).trim();

							Main.print("Chunk: " + chunk);

							current = new Chunk(diff, chunk);
							diff.addChunk(current);
							continue;
						}
						
						if (current != null) {
							current.addRawLine(s);
							Main.print("Code: " + s);
						} else {
							Main.print("!Code: " + s);
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return diffs;
	}
	
	/**
	 * Returns all tags from this repository
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Tag> getTags() throws Exception {
		return getTags(false);
	}

	/**
	 * Returns all tags from this repository
	 * 
	 * @param update if it should be reloaded and not returned from cache
	 * @return
	 * @throws Exception
	 */
	public List<Tag> getTags(boolean update) throws Exception {
		if (!update && tags != null) {
			return tags;
		} else {
			if (tags == null) {
				tags = new ArrayList<Tag>();
			} else {
				tags.clear();
			}

			List<String> raw = run(new String[] { "git", "tag" });

			for (String stag : raw) {
				List<String> rawtext = run(new String[] { "git", "show", stag });			
				
				Tag tag;

				if (rawtext.get(0).startsWith("commit ")) {
					String commit = rawtext.get(0).substring(7, rawtext.get(0).length());
					tag = new Tag(commit, stag);
					tags.add(tag);
				} else if (rawtext.get(0).startsWith("tag ")) {
					String tagger = rawtext.get(1);
					String date = rawtext.get(2);

					String message = "";

					int i = 3; // skip empty line at index 3
					String line = null;

					while (rawtext.size() >= i + 1 && !(line = rawtext.get(i++)).startsWith("commit ")) {
						System.out.println("LINE: " + line);
						if (line.startsWith("diff --git")) {
							break;
						} else {
							message += line + "\n";
						}
					}

					String commit = line.substring(7, line.length());
					
					tag = new Tag(commit, stag, message, tagger, date);
					tags.add(tag);
				}
			}

			return tags;
		}
	}

	public List<String> run(String c) throws Exception {
		return run(c.split(" "));
	}

	/**
	 * Executes process with specified arguments
	 * 
	 * @param c
	 * @return ArrayList with all lines that has been read, empty list if none
	 * @throws Exception
	 */
	public List<String> run(String[] c) throws Exception {
		ProcessBuilder pb = new ProcessBuilder(c);
		pb.directory(folder);
		Process p = pb.start();

		List<String> lines = new ArrayList<String>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;

		while ((line = reader.readLine()) != null) {
			if (line.length() > 0) {
				lines.add(line);
			}
		}

		reader.close();

		return lines;
	}
	
	/**
	 * Reads binary input
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public byte[] readBinary(String[] c) throws Exception {
		ProcessBuilder pb = new ProcessBuilder(c);
		pb.directory(folder);
		Process p = pb.start();

		InputStream is = p.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[1024];
		int i;
		
		while ((i = is.read(buffer)) != -1) {
			baos.write(buffer, 0, i);
		}
		
		return baos.toByteArray();
	}

	/**
	 * Returns if we have unstaged files or changes
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean hasUnstagedFiles() throws Exception {
		List<String> raw = getStatus();

		return raw.size() > 0;
	}

	/**
	 * Gets raw statuses
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getStatus() throws Exception {
		return run(new String[] { "git", "status", "--short", "--porcelain" });
	}

	/**
	 * Parses status to changes
	 * 
	 * @return list of changes
	 * @throws Exception
	 */
	public List<Change> parseStatus() throws Exception {
		List<String> raw = getStatus();
		List<Change> changes = new ArrayList<Change>();

		for (String s : raw) {
			String xy = s.substring(0, 2);
			String path = s.substring(3, s.length());
			
			List<Type> type = Change.getType(xy);
			
			if (path.contains(" -> ")) {
				path = path.split(" -> ")[1];
				type.clear();
				type.add(Change.Type.STAGED_RENAME);
			}
			changes.add(new Change(type, path));
		}

		return changes;
	}

	/**
	 * Stage file
	 * 
	 * @param path
	 *            repo path
	 * @throws Exception
	 */
	public void stage(String path) throws Exception {
		List<String> raw = run(new String[] { "git", "add", path });

		for (String s : raw) {
			Main.print(s);
		}
	}

	/**
	 * Unstage file
	 * 
	 * @param path
	 *            repo path
	 * @throws Exception
	 */
	public void unstage(String path) throws Exception {
		List<String> raw = run(new String[] { "git", "reset", path });

		for (String s : raw) {
			Main.print(s);
		}
	}
	
	public void commit(String message) throws Exception {
		commit(message, false);
	}
	
	public void commit(String message, boolean amend) throws Exception {
		List<String> raw;
		
		if (amend) {
			raw = run(new String[] { "git", "commit", "--amend", "-m", message});
		} else {
			raw = run(new String[] { "git", "commit", "-m", message} );
		}
	}
	
	public List<Remote> getRemotes() throws Exception {
		List<Remote> remotes = new ArrayList<Remote>();
		
		List<String> raw = run(new String[] { "git", "remote", "--verbose"} );
		
		for (String line : raw) {
			String[] split = line.split(" ")[0].split("\t");
			String name = split[0];
			String path = split[1];
			
			System.out.println(name + ", " + path);
			
			Remote remote = new Remote(this, name, path);
			
			if (!remotes.contains(remote)) {
				remotes.add(remote);
			}	
		}
		
		return remotes;
	}
	
	/**
	 * Adds a remote to this repository
	 * @param name
	 * @param path
	 * @throws Exception
	 */
	public void addRemote(String name, String path) throws Exception {
		run(new String[] { "git", "remote", "add", name, path });
	}
	
	/**
	 * Removes a remote from this repository
	 * @param name
	 * @throws Exception
	 */
	public void removeRemote(String name) throws Exception {
		run(new String[] { "git", "remote", "remove", name });
	}
	
	/**
	 * Sets a new url for remote in this repository
	 * @param name
	 * @param path
	 * @throws Exception
	 */
	public void editRemote(String name, String path) throws Exception {
		run(new String[] { "git", "remote", "set-url", name, path });
	}


	/**
	 * Create new empty repository
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		List<String> raw = run(new String[] { "git", "init" });

		for (String s : raw) {
			Main.print(s);
		}
	}

	/**
	 * Gets file content at commit c
	 * 
	 * @param c
	 * @param repopath
	 * @return
	 * @throws Exception
	 * @throws FileNotFoundException
	 *             if invalid hash or path
	 */
	public String getFileAt(Commit c, String repopath) throws Exception {
		List<String> raw = run(new String[] { "git", "show", c.getHash() + ":" + repopath });

		System.out.println("git show " + c.getHash() + ":" + repopath);
		if (raw.size() == 0 || raw.size() > 0 && raw.get(0).startsWith("fatal: Path ") || raw.size() > 0 && raw.get(0).startsWith("fatal: Invalid object name")) {
			throw new FileNotFoundException(raw.get(0));
		}

		StringBuilder sb = new StringBuilder();

		for (String s : raw) {
			sb.append(s);
			sb.append(System.getProperty("line.separator"));
		}

		return sb.toString();
	}
	
	/**
	 * Returns author (if not repo specific, uses global)
	 * @return
	 * @throws Exception
	 */
	public String getAuthorName() throws Exception {
		return run(new String[] { "git", "config", "user.name" }).get(0);
	}
	
	/**
	 * Returns author email (if not repo specific, uses global)
	 * @return
	 * @throws Exception
	 */
	public String getAuthorEmail() throws Exception {
		return run(new String[] { "git", "config", "user.email" }).get(0);
	}
	
	/**
	 * Returns combined string of author and author email
	 * @return
	 * @throws Exception
	 */
	public String getAuthorString() throws Exception {
		return getAuthorName() + " <" + getAuthorEmail() + ">";
	}
	
	/**
	 * Returns true if folder is a valid git repository
	 * @return
	 */
	public boolean isValidRepo() {
		List<String> lines = null;
		try {
			lines = run(new String[] { "git", "rev-parse", "--is-inside-work-tree" } );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lines != null && lines.get(0).equalsIgnoreCase("true");
	}

	public File getFolder() {
		return this.folder;
	}

	public String getName() {
		return getFolder().getName();
	}

}

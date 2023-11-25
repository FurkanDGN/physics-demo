package me.furkandgn.freetype2;

/**
 * This is a simple class wich contains the version information about FreeType.
 */
public class LibraryVersion {

  private final int major;
  private final int minor;
  private final int patch; // Example: 2.6.0

  public LibraryVersion(int major, int minor, int patch) {
    this.major = major;
    this.minor = minor;
    this.patch = patch;
  }

  public int getMajor() {
    return this.major;
  }

  public int getMinor() {
    return this.minor;
  }

  public int getPatch() {
    return this.patch;
  }

  @Override
  public String toString() {
    return this.major + "." + this.minor + "." + this.patch;
  }
}
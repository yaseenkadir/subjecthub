export class User {
  username: string;
  email: string;
  admin: boolean;

  constructor(username?: string, email?: string, admin?: boolean) {
    this.username = username;
    this.email = email;
    this.admin = (admin == null ? false : admin);
  }

  equals(other: User): boolean {
    if (other == null || other ! instanceof User) {
      return false;
    }
    return this.email == other.email &&
      this.username == other.username &&
      this.admin == other.admin;
  }
}

export class User {
  id: number;
  username: string;
  email: string;
  admin: boolean;

  constructor(id?: number, username?: string, email?: string, admin?: boolean) {
    this.id = id;
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

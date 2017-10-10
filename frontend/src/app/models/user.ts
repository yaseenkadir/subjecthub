
export class User {
    username: string;
    email: string;

    constructor(username?: string, email?: string) {
        this.username = username;
        this.email = email;
    }

    equals(other: User): boolean {
        if (other == null || other !instanceof User) {
            return false;
        }
        return this.email == other.email && this.username == other.username;
    }
}

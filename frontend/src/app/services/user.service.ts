
import {Injectable} from "@angular/core";
import {User} from "../models/user";
import {JwtHelper} from "angular2-jwt";

@Injectable()
export class UserService {
    private user: User;
    private jwtHelper: JwtHelper;
    private token: string;
    private parsedToken: any;
    private expiry: Date;

    private static JWT_STORAGE_KEY: string = 'subjecthubjwtkey';
    private static USER_STORAGE_KEY: string = 'subjecthubuserkey';

    constructor() {
        this.jwtHelper = new JwtHelper();

        let persistedToken = localStorage.getItem(UserService.JWT_STORAGE_KEY);
        if (persistedToken != null) {
            console.log('Token retrieved from local storage.');
            this.setToken(persistedToken);
        }

        let persistedUser = localStorage.getItem(UserService.USER_STORAGE_KEY);
        if (persistedUser != null) {
            console.log('Retrieved user from local storage');
            let user = JSON.parse(persistedUser) as User;
            // Casting `as {Class}` doesn't really do anything. We need to use a serializer or
            // manually instantiate it, like we are doing below. TODO: Use a serializer
            this.user = new User(user.username, user.email);
        }
    }

    /**
     * Returns true if the user is logged in. This should always be checked before accessing the
     * user field.
     * @returns {boolean}
     */
    isLoggedIn(): boolean {
        // Not sure how javascript compares date objects
        return this.user != null && this.expiry != null && new Date() < this.expiry;
    }

    setToken(token: string) {
        console.log("Token is being set.");
        this.token = token;
        this.parsedToken = this.jwtHelper.decodeToken(token);
        this.expiry = new Date(this.parsedToken.exp * 1000);
        console.log(`Received token for subject: ${this.parsedToken.sub}`);
        localStorage.setItem(UserService.JWT_STORAGE_KEY, token);
    }

    getToken(): string {
        return this.token;
    }

    setUser(user: User) {
        let castedUser = new User(user.username, user.email);
        console.log(`Setting user: User{username=${user.username}, email=${user.email}}`);
        if (!castedUser.equals(this.user)) {
            // Update user locally.
            console.log('User details are being updated in local storage.');
            localStorage.setItem(UserService.USER_STORAGE_KEY, JSON.stringify(user));
        }
        this.user = user;
    }

    getUser() {
        return this.user;
    }
}

import {Injectable} from '@angular/core';
import {Headers, Http} from '@angular/http';

import {environment} from '../../environments/environment';
import {SubjectHubApiResponse} from '../models/subject-hub-api-response';
import {LoginResponse} from '../models/login-response';
import {JwtHelper} from 'angular2-jwt';
import {User} from '../models/user';

@Injectable()
export class AuthService {
    user: User;
    private jwtHelper: JwtHelper;
    private tokenString: string;
    private token: any;
    private expiry: Date;

    constructor(private http: Http) {
        this.jwtHelper = new JwtHelper();
        this.user = null;
        this.token = null;
    }

    /**
     * Authenticates a user with the backend. Input validation should be performed by caller.
     * @param {string} username
     * @param {string} password
     * @returns {Promise<SubjectHubApiResponse<LoginResponse>>}
     */
    authenticate(username: string, password: string): Promise<LoginResponse> {
        let requestBody = {'username': username, 'password': password};

        return this.http.post(environment.API_URL + '/auth/authenticate', requestBody)
            .toPromise()
            // Caller handles error
            .then(r => this.loginSuccess(r));
    }

    /**
     * Registers a user with the system. No validation is performed here, it is expected to be
     * performed by caller.
     * @param {string} username
     * @param {string} password
     * @param {string} email
     * @returns {Promise<SubjectHubApiResponse>}
     */
    register(username: string, password: string, email: string): Promise<void> {
        let request = {'username': username, 'password': password, 'email': email};
        console.log(`Attempting to register [${username}, ${email}, ${password}]`);

        return this.http.post(environment.API_URL + '/auth/register', request)
            .toPromise()
            // We do nothing with the response currently, but it may return details in the future.
            .then(() => {});
    }

    /**
     * Gets user details from the server. Assumes user is already logged in.
     * @returns {Promise<SubjectHubApiResponse<User>>}
     */
    private getUser(): Promise<SubjectHubApiResponse<User>> {

        let headers = new Headers();
        headers.append('Authorization', this.tokenString);

        return this.http.get(`${environment.API_URL}/auth/self`, {headers: headers})
            .toPromise()
            .then(response => {
                let user = response.json() as User;
                console.log('User successfully fetched');
                return new SubjectHubApiResponse<User>(true, user, null);
            });
    }

    private fetchUser() {
        this.getUser()
            .then((response) => this.user = response.response as User)
            .catch((e) => {
                throw new Error('Successfully logged in but unable to retrieve user.')
            })
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

    private loginSuccess(response: any): LoginResponse {
        console.log(`Successfully authenticated`);
        console.log(`jwt is ${response.json()['token']}`);

        let loginResponse = response.json() as LoginResponse;
        let token = loginResponse.token;
        this.tokenString = token;
        this.token = this.jwtHelper.decodeToken(token);
        this.expiry = new Date(this.token.exp * 1000);

        this.fetchUser();
        return loginResponse;
    }
}

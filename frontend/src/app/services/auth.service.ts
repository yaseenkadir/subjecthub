import {Injectable} from '@angular/core';
import {Headers, Http} from '@angular/http';

import {environment} from '../../environments/environment';
import {SubjectHubApiResponse} from '../models/subject-hub-api-response';
import {LoginResponse} from '../models/login-response';
import {User} from '../models/user';
import {UserService} from "./user.service";

@Injectable()
export class AuthService {

    constructor(private http: Http, private userService: UserService) {
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
     * Gets user details from the server.
     * @param {string} token
     * @returns {Promise<User>}
     */
    getUser(token: string): Promise<User> {
        let headers = new Headers();
        headers.append('Authorization', token);

        return this.http.get(`${environment.API_URL}/auth/self`, {headers: headers})
            .toPromise()
            .then(response => {return response.json() as User;});
    }

    private loginSuccess(response: any): LoginResponse {
        console.log(`Successfully authenticated`);
        console.log(`jwt is ${response.json()['token']}`);

        let loginResponse = response.json() as LoginResponse;
        let token = loginResponse.token;
        this.userService.setToken(token);
        this.getUser(token)
            .then(u => {
                console.log(`fetched User{username=${u.username}, email=${u.email}}`);
                this.userService.setUser(u);
            })
            .catch((e) => {
                console.log('Unable to fetch u');
                console.log(e);
                throw new Error('Really unexpected error occurred when fetching user!')
            });
        return loginResponse;
    }
}

import {Injectable} from '@angular/core';
import {Headers, Http} from '@angular/http';

import {environment} from '../../environments/environment';
import {SubjectHubApiResponse} from '../models/subject-hub-api-response';
import {LoginResponse} from '../models/login-response';
import {JwtHelper} from 'angular2-jwt';
import {User} from '../models/user';
import {HttpStatus} from '../utils/http-status';
import {ApiErrorHandler} from '../utils/api-error-handler';

@Injectable()
export class UserService {
    user: User;
    private jwtHelper: JwtHelper;
    private tokenString: string;
    private token: any;
    private expiry: Date;

    constructor(private http: Http, private apiErrorHandler: ApiErrorHandler) {
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
    authenticate(username: string, password: string): Promise<SubjectHubApiResponse<LoginResponse>> {
        let requestBody = {'username': username, 'password': password};

        return this.http.post(environment.API_URL + '/auth/authenticate', requestBody)
            .toPromise()
            .then(response => {
                if (response.status == 200) {
                    console.log(`Successfully authenticated ${username}.`);
                    console.log(`jwt is ${response.json()['token']}`);

                    let token = (response.json() as LoginResponse).token;
                    this.tokenString = token;
                    this.token = this.jwtHelper.decodeToken(token);
                    this.expiry = new Date(this.token.exp * 1000);

                    this.fetchUser();
                    return new SubjectHubApiResponse<LoginResponse>(
                        true, response.json() as LoginResponse, null);
                } else {
                    console.log('Login failed.');
                    return this.apiErrorHandler.handleApiError(response);
                }
            })
            .catch(e => {
                console.log('Login failed. Caught exception: ' + e);
                return this.apiErrorHandler.handleApiError(e);
            });
    }

    /**
     * Registers a user with the system. No validation is performed here, it is expected to be
     * performed by caller.
     * @param {string} username
     * @param {string} password
     * @param {string} email
     * @returns {Promise<SubjectHubApiResponse>}
     */
    register(username: string, password: string, email: string): Promise<SubjectHubApiResponse<void>> {
        let request = {'username': username, 'password': password, 'email': email};
        console.log(`Attempting to register [${username}, ${email}, ${password}]`);

        return this.http.post(environment.API_URL + '/auth/register', request)
            .toPromise()
            .then(response => {
                if (response.status == HttpStatus.OK) {
                    return new SubjectHubApiResponse(true, null, null);
                } else {
                    console.log('Registration attempt failed.');
                    return this.apiErrorHandler.handleApiError(response);
                }
            })
            .catch(e => {
                console.log('Registration attempt failed.');
                return this.apiErrorHandler.handleApiError(e);
            });
    }

    /**
     * Gets user details from the server. Assumes user is already logged in.
     * @returns {Promise<SubjectHubApiResponse<User>>}
     */
    getUser(): Promise<SubjectHubApiResponse<User>> {

        if (!this.isLoggedIn()) {
            throw new Error("Must be logged in to get user.");
        }

        let headers = new Headers();
        headers.append('Authorization', this.tokenString);

        return this.http.get(`${environment.API_URL}/auth/self`, {headers: headers})
            .toPromise()
            .then(response => {
                if (response.status == HttpStatus.OK) {
                    let user = response.json() as User;
                    console.log('User successfully fetched');
                    return new SubjectHubApiResponse<User>(true, user, null);
                } else {
                    return this.apiErrorHandler.handleApiError(response);
                }
            })
            .catch( e => {
                return this.apiErrorHandler.handleApiError(e);
            });
    }

    private fetchUser() {
        this.getUser().then(response => {
            if (response.isSuccessful()) {
                this.user = response.response as User;
            } else {
                // this shouldn't ever, ever happen
                throw new Error('Successfully logged in but unable to retrieve user.')
            }
        });
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
}

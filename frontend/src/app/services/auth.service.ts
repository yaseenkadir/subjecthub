import {Injectable} from '@angular/core';
import {Http} from "@angular/http";

import {environment} from '../../environments/environment';
import {SubjectHubApiResponse} from "../models/subject-hub-api-response";
import {LoginResponse} from "../models/login-response";

@Injectable()
export class AuthService {
    constructor(private http: Http) {
    }

    /**
     * Authenticates a user with the backend. Input validation should be performed by caller.
     * @param {string} username
     * @param {string} password
     * @returns {Promise<SubjectHubApiResponse<LoginResponse>>}
     */
    authenticate(username: string, password: string): Promise<SubjectHubApiResponse<LoginResponse>> {
        let requestBody = {"username": username, "password": password};
        return this.http.post(environment.API_URL + "/auth/authenticate", requestBody)
            .toPromise()
            .then(response => {
                if (response.status == 200) {
                    console.log(`Successfully authenticated ${username}.`);
                    console.log(`jwt is ${response.json()['token']}`);
                    return new SubjectHubApiResponse(true, response.json() as LoginResponse);
                } else {
                    console.log("Auth request failed.");
                    console.log(response.status);
                    return new SubjectHubApiResponse(false, null, response.json()['message']);
                }
            })
            .catch(e => {
                console.log("Login failed. Caught exception: " + e);
                console.log(e);
                // If e has status 0, it's likely due to no internet connection to backend.
                if (e.status == 0) {
                    return new SubjectHubApiResponse(false, null, "Unable to connect to server.");
                }
                return new SubjectHubApiResponse(false, null, e.json()['message']);
            });
    }

    /**
     * Registers a user with the system. No validation is performed here, it is expected to be performed by caller.
     * @param {string} username
     * @param {string} password
     * @param {string} email
     * @returns {Promise<SubjectHubApiResponse>}
     */
    register(username: string, password: string, email: string): Promise<SubjectHubApiResponse<void>> {
        let request = {"username": username, "password": password, "email": email};
        console.log(`Attempting to register [${username}, ${email}]`);

        return this.http.post(environment.API_URL + "/auth/register", request)
            .toPromise()
            .then(response => {
                return new SubjectHubApiResponse(true, null);
            })
            .catch(e => {
                console.log("Register request failed. Caught exception: " + e);
                console.log(e);
                let errorMessage: string = null;

                if (e.status == 0) {
                    // If e has status 0, it's likely due to no internet connection to backend.
                    errorMessage = "Unable to connect to server.";
                } else {
                    errorMessage = e.json()['message'];
                }
                return new SubjectHubApiResponse(false, null, errorMessage);
            });
    }
}

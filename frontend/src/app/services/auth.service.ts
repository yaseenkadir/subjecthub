import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { LoginResponse } from '../models/login-response';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/user';


@Injectable()
export class AuthService {

  constructor(private http: HttpClient) {
  }

  login(username: string, password: string): Promise<LoginResponse> {
    let requestBody = {'username': username, 'password': password};

    return this.http.post(environment.API_URL + '/auth/authenticate', requestBody)
      .toPromise()
      // Caller handles error
      .then(res => res);
  }

  register(username: string, password: string, email: string): Promise<void> {
    let request = {'username': username, 'password': password, 'email': email};
    console.log(`Attempting to register [${username}, ${email}, ${password}]`);

    return this.http.post(environment.API_URL + '/auth/register', request)
      .toPromise()
      // We do nothing with the response currently, but it may return details in the future.
      .then(() => {});
  }

  saveToken = token => {
    window.sessionStorage["token"] = token;
  };

  getToken = () => {
    return window.sessionStorage["token"];
  };

  deleteToken = () => {
    delete window.sessionStorage["token"];
  };

  logout = () => {
    delete window.sessionStorage["token"];
    location.reload();
  };

  isLoggedIn = () => {
    let token = this.getToken();
    let payload;

    if (token) {
      try {
        payload = token.split(".")[1];
        payload = window.atob(payload);
        payload = JSON.parse(payload);

        let hasExpired = payload.exp < Date.now() / 1000;

        if (hasExpired) this.logout();

        return !hasExpired;
      } catch (err) {
        return false;
      }
    } else {
      return false;
    }
  };

  currentUser = () => {
    if (this.isLoggedIn()) {
      try {
        let token = this.getToken();
        let payload = token.split(".")[1];
        payload = window.atob(payload);
        payload = JSON.parse(payload);

        let username = payload.sub;
        // TODO-HIGH: Add user roles to jwt token
        // temporary hack to determine whether a user is an admin or not
        const isAdmin = username === 'admin';
        return new User(username, 'abc@example.com', isAdmin);
      } catch (err) {
        throw new Error(err);
      }
    }
  };
}

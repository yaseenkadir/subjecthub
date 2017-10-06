import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../services/user.service";
import {Consts} from "../config/consts";
import {Utils} from "../utils/utils";
import {ToastrService} from "ngx-toastr";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css'],
    providers: [UserService]
})

export class LoginComponent implements OnInit {

    loginForm: FormGroup;

    // Flag fields used to display client side errors
    attemptedLogin: boolean = false;
    displayUsernameError: boolean = false;
    displayPasswordError: boolean = false;

    // Used to display network/server side errors. If not null, display an error.
    authError?: string = null;

    isLoading: boolean = false;

    constructor(private authService: UserService, private toastr: ToastrService, fb: FormBuilder) {

        this.loginForm = fb.group({
            "username": [
                null,
                Validators.compose([
                    Validators.required,
                    Validators.minLength(Consts.MIN_USERNAME_LENGTH),
                    Validators.maxLength(Consts.MAX_USERNAME_LENGTH),
                ])
            ],
            "password": [
                null,
                Validators.compose([
                    Validators.required,
                    Validators.minLength(Consts.MIN_PASSWORD_LENGTH),
                ])]
        });

        // Display password and username errors only when they've passed the min length.
        this.username.valueChanges.subscribe(username => {
            if (!this.displayUsernameError &&
                Utils.exceededMinLength(username, Consts.MIN_USERNAME_LENGTH)) {
                this.displayUsernameError = true;
            }
        });

        this.password.valueChanges.subscribe(password => {
            if (!this.displayPasswordError &&
                Utils.exceededMinLength(password, Consts.MIN_PASSWORD_LENGTH)) {
                this.displayPasswordError = true;
            }
        });
    }

    get username() {
        return this.loginForm.get('username');
    }

    get password() {
        return this.loginForm.get('password');
    }

    login(form) {
        this.attemptedLogin = true;
        this.authError = null;
        if (this.loginForm.valid) {
            console.log("Form is valid. Attempting login.");
            this.isLoading = true;
            this.authService.authenticate(form.username, form.password)
                .then(result => {
                    this.isLoading = false;
                    if (result.isSuccessful()) {
                        console.log("Successfully authenticated user.");
                        this.toastr.success('Logged in', null, {timeOut: 3000});

                    } else {
                        console.log("Login failed");
                        console.log(result.message);
                        this.authError = result.message;
                        this.toastr.error(result.message, 'Login Failed');
                    }
                });
        } else {
            console.log("Login form is invalid. Displaying errors.");
            Utils.markFormAsModified(this.loginForm);
            this.displayUsernameError = true;
            this.displayPasswordError = true;
        }
        return false;
    }

  ngOnInit() {
  }

}

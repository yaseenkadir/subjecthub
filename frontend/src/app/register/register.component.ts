import {Component, OnInit} from '@angular/core';
import {UserService} from "../services/user.service";
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {Consts} from "../config/consts";
import {Utils} from "../utils/utils";
import {ApiErrorHandler} from "../utils/api-error-handler";

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css'],
    providers: [UserService, ApiErrorHandler],
})

export class RegisterComponent implements OnInit {

    registerForm: FormGroup;

    // flags that are used to display errors
    attemptedRegister: boolean = false;
    displayUsernameError: boolean = false;
    displayPasswordError: boolean = false;

    // authError is used to display error message. If not null display.
    authError?: string = null;

    constructor(private authService: UserService, fb: FormBuilder) {

        this.registerForm = fb.group({
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
                ])],
            "email": [
                null,
                Validators.compose([
                    Validators.required,
                    Validators.email]
                )]
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

    ngOnInit() {
    }

    register(form) {
        // Hide error message if it exists.
        this.authError = null;
        this.attemptedRegister = true;

        if (this.registerForm.valid) {
            console.log("Form is valid. Attempting registration.");
            this.authService.register(form.username, form.password, form.email)
                .then(result => {
                    if (result.isSuccessful()) {
                        console.log("Successfully registered user.");
                    } else {
                        this.authError = result.message;
                    }
                });
        } else {
            console.log("Register form is invalid. Displaying errors.");
            Utils.markFormAsModified(this.registerForm);
            this.displayUsernameError = true;
            this.displayPasswordError = true;
        }
        // Returning false so page doesn't change.
        return false;
    }

    get username() {
        return this.registerForm.get('username');
    }

    get password() {
        return this.registerForm.get('password');
    }

    get email() {
        return this.registerForm.get('email');
    }
}

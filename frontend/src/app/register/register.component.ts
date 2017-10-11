import {Component, OnInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {Consts} from "../config/consts";
import {Utils} from "../utils/utils";
import {ToastrService} from "ngx-toastr";
import {Location} from "@angular/common";

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css'],
    providers: [AuthService],
})

export class RegisterComponent implements OnInit {

    registerForm: FormGroup;

    // flags that are used to display errors
    attemptedRegister: boolean = false;
    displayUsernameError: boolean = false;
    displayPasswordError: boolean = false;

    // authError is used to display error message. If not null display.
    authError?: string = null;
    isLoading: boolean = false;

    constructor(private authService: AuthService, private toastr: ToastrService,
                private location: Location, fb: FormBuilder) {

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
            this.isLoading = true;
            this.authService.register(form.username, form.password, form.email)
                .then(() => this.registerSuccess())
                .catch((e) => this.registerError(e));
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

    private registerSuccess() {
        this.isLoading = false;
        console.log('Successfully registered user.');
        this.toastr.success('Successfully Registered', null, {timeOut: 3000})
        this.location.back();
    }

    private registerError(e) {
        this.isLoading = false;
        let errorMessage = Utils.getApiErrorMessage(e);
        console.log(`Registration failed due to: ${errorMessage}`);
        this.toastr.error(errorMessage, 'Registration failed');
    }
}


import {FormGroup} from "@angular/forms";

export class Utils {

    static exceededMinLength(value: string, minLength: number) {
        return (value.length >= minLength);
    }

    static markFormAsModified(form: FormGroup) {
        Object.keys(form.controls).forEach(key => {
            let formControl = form.get(key);
            formControl.markAsTouched();
            formControl.markAsDirty();
        })
    }

    static getApiErrorMessage(e: any): string {
        if (e.status == 0) {
            // If e has status 0, it's likely due to no internet connection to backend.
            return "Unable to connect to server.";
        } else {
            return e.json()['message'];
        }
    }
}

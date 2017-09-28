
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
}

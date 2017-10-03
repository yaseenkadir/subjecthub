/**
 * Model class that wraps backend API responses.
 *
 * Very naive implementation currently, but does a decent job. Any part of the code that receives
 * this response should know how to handle the success case. If success is false, the receiver
 * should display the contents of the message field.
 */
export class SubjectHubApiResponse<T> {
    success: boolean = false;
    response?: T;
    message?: string = null;

    constructor(success: boolean, response: T, message: string = null) {
        this.success = success;
        this.response = response;
        this.message = message;
    }

    isSuccessful(): boolean {
        return this.success;
    }
}

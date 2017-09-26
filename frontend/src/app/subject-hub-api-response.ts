/**
 * Model class that wraps backend API responses.
 *
 * Very naive implementation currently, but does a decent job. Any part of the code that receives this response should
 * know how to handle the success case. If it's not successful they should simply display any error message using
 * response['message'].
 */
export class SubjectHubApiResponse {
    success: boolean = false;
    response: any;

    constructor(success: boolean, response: any) {
        this.success = success;
        this.response = response;
    }

    isSuccessful(): boolean {
        return this.success;
    }
}

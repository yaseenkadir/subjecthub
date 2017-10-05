
import {Injectable} from "@angular/core";
import {SubjectHubApiResponse} from "../models/subject-hub-api-response";

@Injectable()
export class ApiErrorHandler {
    handleApiError(e: any) : SubjectHubApiResponse<void> {
        console.log(e);
        let errorMessage: string;
        if (e.status == 0) {
            // If e has status 0, it's likely due to no internet connection to backend.
            errorMessage = "Unable to connect to server.";
        } else {
            errorMessage = e.json()['message'];
        }
        return new SubjectHubApiResponse(false, null, errorMessage);
    }
}

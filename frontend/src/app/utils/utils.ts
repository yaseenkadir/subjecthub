import { FormGroup } from "@angular/forms";
import { environment } from '../../environments/environment';

export class Utils {

  static exceededMinLength(value: string, minLength: number | string) {
    return (value.length >= minLength);
  }

  static markFormAsModified(form: FormGroup) {
    Object.keys(form.controls).forEach(key => {
      let formControl = form.get(key);
      formControl.markAsTouched();
      formControl.markAsDirty();
    });
  }

  static getApiErrorMessage(e: any): string {
    if (e.status == 0) {
      // If e has status 0, it's likely due to no internet connection to backend.
      return "Unable to connect to server.";
    } else {
      return e.error.message;
    }
  }
}


export class ApiUrlUtils {

  static baseApiUrl(): string {
    return environment.API_URL;
  }

  static universitiesUrl() {
    return ApiUrlUtils.baseApiUrl() + '/universities';
  }

  static buildUniversityUrl(universityId: number | string) {
    return ApiUrlUtils.universitiesUrl() + '/university/' + universityId;
  }

  static buildFacultiesUrl(universityId: number | string) {
    return ApiUrlUtils.buildUniversityUrl(universityId) + '/faculties';
  }

  static buildFacultyUrl(universityId: number | string, facultyId: number | string) {
    return ApiUrlUtils.buildFacultiesUrl(universityId) + '/faculty/' + facultyId;
  }

  static buildSubjectsUrl(universityId: number | string) {
    return ApiUrlUtils.buildUniversityUrl(universityId) + '/subjects';
  }

  static buildSubjectUrl(universityId: number | string, subjectId: number | string) {
    return ApiUrlUtils.buildSubjectsUrl(universityId) + '/subject/' + subjectId;
  }

  static buildAssessmentsUrl(universityId: number | string, subjectId: number | string) {
    return ApiUrlUtils.buildSubjectUrl(universityId, subjectId) + '/assessments';
  }

  static buildAssessmentUrl(universityId: number | string, subjectId: number | string,
                            assessmentId: number | string) {
    return ApiUrlUtils.buildAssessmentsUrl(universityId, subjectId) + '/assessment/' + assessmentId;
  }

  static buildCommentsUrl(universityId: number | string, subjectId: number | string) {
    return ApiUrlUtils.buildSubjectUrl(universityId, subjectId) + '/comments';
  }

  static buildCommentUrl(universityId: number | string, subjectId: number | string,
                         commentId: number | string) {
    return ApiUrlUtils.buildCommentsUrl(universityId, subjectId) + '/comment/' + commentId;
  }
}

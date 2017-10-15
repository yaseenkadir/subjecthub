import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { AlertModule } from 'ngx-bootstrap';
import { ModalModule } from 'ngx-bootstrap/modal';
import { ToastrModule } from 'ngx-toastr';

import { FilterPipe } from './pipes/filter.pipe';

import { AppRoutingModule } from './app.module.routes';
import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import { SearchBarComponent } from './search-bar/search-bar.component';
import { FuseSearchBarComponent } from './search-bar/fuse-search-bar.component';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { AddTagComponent } from './add-tag/add-tag.component';
import { SpinnerComponent } from './spinner/spinner.component';
import { SubjectCommentsComponent } from './subject-comments/subject-comments.component';
import { UniversitiesComponent } from './universities/universities.component';
import { SubjectDetailsComponent } from './subject-details/subject-details.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SearchPageComponent } from './search-page/search-page.component';
import { AuthInterceptor } from './utils/auth-interceptor';
import { AuthService } from './services/auth.service';
import { UniversitiesAdminComponent } from './universities-admin/universities-admin.component';
import { EditUniversityComponent } from './edit-university/edit-university.component';
import { UniversityEditComponent } from './university-edit/university-edit.component';
import { CreateFacultyComponent } from './create-faculty/create-faculty.component';
import { CreateSubjectComponent } from './create-subject/create-subject.component';



@NgModule({
  imports: [
    AppRoutingModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    AlertModule.forRoot(),

    // Needed for toasts
    BrowserAnimationsModule,
    ToastrModule.forRoot(
      {
        closeButton: true,
        toastClass: 'toast my-toast',
        positionClass: 'toast-top-center'
      }),
    ModalModule.forRoot(),
  ],
  declarations: [
    AppComponent,
    NavigationComponent,
    SearchBarComponent,
    FuseSearchBarComponent,
    RegisterComponent,
    LoginComponent,
    AddTagComponent,
    SpinnerComponent,
    SubjectCommentsComponent,
    UniversitiesComponent,
    SubjectDetailsComponent,
    DashboardComponent,
    SearchPageComponent,
    EditUniversityComponent,
    UniversitiesAdminComponent,
    UniversityEditComponent,
    FilterPipe,
    CreateFacultyComponent,
    CreateSubjectComponent
  ],
  bootstrap: [AppComponent],
  providers: [
    AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    }
  ],
  entryComponents: [

    EditUniversityComponent,
    CreateFacultyComponent,
    CreateSubjectComponent
  ]
})
export class AppModule {
}

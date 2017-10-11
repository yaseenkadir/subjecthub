import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { HttpModule } from "@angular/http";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";

import { AppRoutingModule } from './app.module.routes';

import { AppComponent } from "./app.component";
import { NavigationComponent } from "../navigation/navigation.component";
import { SearchBarComponent } from "../search-bar/search-bar.component";
import { FuseSearchBarComponent } from "../search-bar/fuse-search-bar.component";
import { RegisterComponent } from './register/register.component';
import {AlertModule} from "ngx-bootstrap";
import { LoginComponent } from './login/login.component';
import { AddTagComponent } from './add-tag/add-tag.component';
import { SpinnerComponent } from './spinner/spinner.component';
import { SubjectCommentsComponent } from './subject-comments/subject-comments.component';


import { UniversitiesComponent} from './universities/universities.component';
import {RouterModule} from "@angular/router";
import {ToastrModule} from "ngx-toastr";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { SubjectDetailsComponent } from './subject-details/subject-details.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SearchPageComponent } from './search-page/search-page.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthInterceptor } from './utils/auth-interceptor';
import { AuthService } from './services/auth.service';


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
  ],
  bootstrap: [AppComponent],
  providers: [
    AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    }
  ]
})
export class AppModule {}

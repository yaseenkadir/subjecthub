import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { HttpModule } from "@angular/http";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";

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
import {ApiErrorHandler} from "./utils/api-error-handler";
import {RouterModule} from "@angular/router";
import {ToastrModule} from "ngx-toastr";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

@NgModule({
  imports: [
      RouterModule.forRoot([
          {
              path: 'login',
              component: LoginComponent
          },
          {
              path: 'register',
              component: RegisterComponent
          },
          {
              path: '',
              redirectTo: 'home',
              pathMatch: 'full'
          },
          {
              path: 'home',
              component: SearchBarComponent
          },
        ]
      ),
      NgbModule.forRoot(),
      BrowserModule,
      FormsModule,
      HttpModule,
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
  ],
  providers: [ApiErrorHandler],
  bootstrap: [AppComponent]
})
export class AppModule {}

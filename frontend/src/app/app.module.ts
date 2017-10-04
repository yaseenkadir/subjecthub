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
import {ApiErrorHandler} from "./utils/api-error-handler";

@NgModule({
  imports: [
      NgbModule.forRoot(),
      BrowserModule,
      FormsModule,
      HttpModule,
      ReactiveFormsModule,
      AlertModule.forRoot()
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
    SubjectCommentsComponent
  ],
  providers: [ApiErrorHandler],
  bootstrap: [AppComponent]
})
export class AppModule {}

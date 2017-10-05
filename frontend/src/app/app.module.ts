import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { HttpModule } from "@angular/http";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
// Imports for loading & configuring the in-memory web api
import { InMemoryWebApiModule } from "angular-in-memory-web-api";
import { InMemoryDataService } from "./utils/in-memory-data.service";

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
import { FacultyComponent } from './faculty/faculty.component';

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
    SubjectCommentsComponent,
    FacultyComponent
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}

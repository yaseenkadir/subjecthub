import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { HttpModule } from "@angular/http";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
// Imports for loading & configuring the in-memory web api
import { InMemoryWebApiModule } from "angular-in-memory-web-api";
import { InMemoryDataService } from "./in-memory-data.service";

import { AppComponent } from "./app.component";
import { NavigationComponent } from "../navigation/navigation.component";
import { SearchBarComponent } from "../search-bar/search-bar.component";
import { FuseSearchBarComponent } from "../search-bar/fuse-search-bar.component";

@NgModule({
  imports: [NgbModule.forRoot(), BrowserModule, FormsModule, HttpModule],
  declarations: [
    AppComponent,
    NavigationComponent,
    SearchBarComponent,
    FuseSearchBarComponent
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}

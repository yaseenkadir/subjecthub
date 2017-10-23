import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'UTS Subject Hub';


  ngOnInit(): void {
    // Hide spinner that was displayed during bundle load
    //
    document.getElementById('loadingContainer').style.display = 'None';
  }
}

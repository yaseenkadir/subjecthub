import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  isAdmin: boolean = false;
  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.isAdmin = this.authService.isAdmin();
  }
}

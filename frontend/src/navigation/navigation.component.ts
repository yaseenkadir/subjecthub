import {Component, OnInit} from '@angular/core';
import {AuthService} from "../app/services/auth.service";
import {User} from "../app/models/user";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css'],
  providers: [AuthService]
})
export class NavigationComponent implements OnInit {

  private user: any;
  private userSubscription: Subscription;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.user = this.authService.currentUser() || null;
  }

  logout() {
    this.authService.logout();
  }
}

import {Component, OnInit} from '@angular/core';
import {UserService} from "../app/services/user.service";
import {User} from "../app/models/user";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css'],
  providers: [UserService]
})
export class NavigationComponent implements OnInit {

  private user: User;
  private userSubscription: Subscription;

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.user = null;

    console.log("Nav bar component initialised.");
    this.userSubscription = this.userService.userObservable$
      .subscribe((user: User) => {
          console.log("Received emitter event");
          this.user = user;
        }
      );
  }

  ngOnDestroy() {
    this.userSubscription.unsubscribe();
  }

  logout() {
    this.userService.logout();
  }
}

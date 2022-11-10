import { Component, OnInit } from '@angular/core';
import {ApiService} from "../../services/api/api.service";
import {User} from "../../models/user.model";
import {HttpErrorResponse} from "@angular/common/http";
import {NotificationService} from "../../services/notification.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  username: string = '';
  password: string = '';

  constructor(private api: ApiService,
              private notification: NotificationService,
              private router: Router) { }

  ngOnInit(): void {
  }

  login() {
    if(this.username === '' || this.password === '') {
      this.notification.send(['mat-snackbar-error'], "Username or password cannot be empty!")
      return;
    }

    this.api.post<User>("/login", {username: this.username, password: this.password}).subscribe(user => {
      this.go("/index")
    }, (err: HttpErrorResponse) => {
      this.handleStatusCodeResponse(err.status)
    })
  }

  go(url: string) {
    this.router.navigate([url]);
  }

  private handleStatusCodeResponse(code: number) {
    switch (code) {
      case 404:
        this.notification.send(['mat-snackbar-error'], "No user associated with this credentials!")
        return;
      case 409:
        this.notification.send(['mat-snackbar-error'], "This user does not have admin permissions!")

        return;
      default:
        this.notification.send(['mat-snackbar-error'], "Unexpected error occurred!")
        return;
    }
  }
}

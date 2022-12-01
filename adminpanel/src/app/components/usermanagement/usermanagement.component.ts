import {Component, Inject, OnInit} from '@angular/core';
import {ApiService} from "../../services/api/api.service";
import {User} from "../../models/user.model";
import {Sort} from "@angular/material/sort";
import {compare, instantToReadableConverter} from "../../global/methods.global";
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";
import {YesnodialogComponent} from "../../dialogs/yesnodialog/yesnodialog.component";

@Component({
  selector: 'app-usermanagement',
  templateUrl: './usermanagement.component.html',
  styleUrls: ['./usermanagement.component.scss']
})
export class UsermanagementComponent implements OnInit {

  constructor(private api: ApiService,
              private dialog: MatDialog){ }

  userList: User[] = [];
  displayedColumns: string[] = ['username', 'role', 'token', 'tokenExpiresAt', 'actions'];

  ngOnInit(): void {
    this.api.get<User[]>("/panel/all").subscribe(users => {
      this.userList = users;
      console.log(users)
    });
  }

  readableInstant(instant: string) {
    return instantToReadableConverter(instant)
  }

  sortChange(sort: Sort) {
    const data = this.userList.slice();
    if (!sort.active || sort.direction === '') {
      this.userList = data;
      return;
    }

    this.userList = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'username':
          return compare(a.username, b.username, isAsc);
        case 'role':
          return compare(a.role, b.role, isAsc);
        case 'tokenExpiresAt':
          return compare(a.tokenExpiresAt, b.tokenExpiresAt, isAsc);
        case 'token':
          return compare(a.token, b.token, isAsc);
        default:
          return 0;
      }
    });
  }

  delete(user: User) {
    const dialogRef = this.dialog.open(YesnodialogComponent, {
      data: {user: user}
    });
    dialogRef.afterClosed().subscribe(value => {
      if(value) {
        this.api.delete<User[]>("/panel/delete/" + user.username).subscribe(users => {
          this.userList = users;
        })
      }
    })
  }

  editPassword() {

  }

  edit() {

  }
}

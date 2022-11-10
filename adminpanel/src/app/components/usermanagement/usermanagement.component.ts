import { Component, OnInit } from '@angular/core';
import {ApiService} from "../../services/api/api.service";
import {User} from "../../models/user.model";
import {Sort} from "@angular/material/sort";
import {compare, instantToReadableConverter} from "../../global/methods.global";

@Component({
  selector: 'app-usermanagement',
  templateUrl: './usermanagement.component.html',
  styleUrls: ['./usermanagement.component.scss']
})
export class UsermanagementComponent implements OnInit {

  constructor(private api: ApiService) { }

  userList: User[] = [];

  ngOnInit(): void {
    this.api.get<User[]>("/panel/all").subscribe(users => {
      users.forEach(u => {
        u.tokenExpiresAt = instantToReadableConverter(u.tokenExpiresAt);
      })
      this.userList = users;
      console.log(users)
    });
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

  delete() {

  }

  editPassword() {

  }

  edit() {

  }
}

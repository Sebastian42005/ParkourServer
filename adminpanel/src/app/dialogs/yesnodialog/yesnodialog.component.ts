import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {User} from "../../models/user.model";

@Component({
  selector: 'app-yesnodialog',
  templateUrl: './yesnodialog.component.html',
  styleUrls: ['./yesnodialog.component.scss']
})
export class YesnodialogComponent implements OnInit {

  user: User

  constructor(@Inject(MAT_DIALOG_DATA) public data: {user: User}) {
    this.user = this.data.user;
  }

  ngOnInit(): void {
    console.log(this.data.user)
  }

}

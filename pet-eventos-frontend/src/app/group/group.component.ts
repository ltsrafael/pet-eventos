import { MatTableDataSource } from '@angular/material/table';
import { User } from './../model/User';
import { Router } from '@angular/router';
import { LoginService } from './../login/login.service';
import { GroupService } from './group.service';
import { Group } from './../model/Group';
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css']
})
export class GroupComponent implements OnInit {

  group: Group;
  displayedColumns: string[] =  ['name', 'email', 'edit', 'delete'];
  error: any;
  dataSource: MatTableDataSource<User>;
  user: User;
  userEdited: any;
  editingUser: any;
  addingUser: any;
  userAdded: any;
  viewingGroup: any;
  editingGroup: any;
  groupEdited: any;
  processing: any;

  constructor(private groupService: GroupService, private loginService: LoginService,  private router: Router, private _snackBar: MatSnackBar) {

  }

  ngOnInit(): void {
    this.group = new Group();
    this.user = new User();
    this.loginService.isAuthenticated();
    this.getGroup();
  }

  openSnackBar() {
    this._snackBar.open(this.error, 'Ok');
  }


  getGroup() {
    this.groupService
        .getGroup()
        .subscribe( (response:any) => {
          this.clearState();
          this.group = response;
          this.viewingGroup = true;
          // console.log(this.group);
        }, (error:any) => {
          this.error = error.error.message;
          this.openSnackBar();
        })
  }

  postUser() {
    this.addingUser = false;
    this.processing = true;
    this.loginService
      .postUserAuthenticated(this.user)
      .subscribe( (response:any) => {
        this.clearState();
        this.userAdded = true;
      }, (error:any) => {
        this.addingUser = true;
        this.processing = false;
        this.error = error.error.message;
        this.openSnackBar();
      })
  }


  putUser() {
    this.loginService
      .putUserAuthenticated(this.user)
      .subscribe( (response:any) => {
        this.clearState();
        this.userEdited = true;
      }, (error:any) => {
        this.error = error.error.message;
        this.openSnackBar();
      })
  }

  putGroup() {
    this.group.users = [];
    this.groupService
        .putGroup(this.group)
        .subscribe( (response:any) => {
          this.clearState();
          this.groupEdited = true;
        }, (error:any) => {
          this.error = error.error.message;
          this.openSnackBar();
        })
  }

  editUser(user: User) {
    this.clearState();
    this.editingUser = true;
    this.user = user;
    this.user.group = new Group();
    this.user.group.idGroup = this.group.idGroup;
  }

  addUser() {
    this.clearState();
    this.user = new User();
    this.addingUser = true;
  }

  editGroup() {
    this.clearState();
    this.editingGroup = true;
  }

  clearState() {
    this.addingUser = false;
    this.userEdited = false;
    this.editingUser = false;
    this.viewingGroup = false;
    this.userAdded = false;
    this.editingGroup = false;
    this.groupEdited = false;
    this.processing = false;
  }
}

import { GroupService } from './../group/group.service';
import { Group } from './../model/Group';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { LoginService } from './../login/login.service';
import { User } from './../model/User';
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {


  user: User;
  users: User[];
  addingUser: boolean;
  userEdited: boolean;
  editingUser: boolean;
  userAdded: boolean;
  displayedColumnsUser: string[] =  ['name', 'email', 'type', 'group', 'edit', 'delete'];
  displayedColumnsGroup: string[] =  ['name', 'course', 'edit'];
  error: any;
  dataSourceUser: MatTableDataSource<User>;
  dataSourceGroup: MatTableDataSource<Group>;
  listingUsers: boolean;
  groups: Group[];
  idGroup: any;
  listingGroups: boolean;
  group: Group;
  addingGroup: boolean;
  editingGroup:boolean;
  groupAdded: boolean;
  groupEdited: boolean;


  constructor(private groupService: GroupService, private loginService: LoginService,  private router: Router, private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.getUsers();
    this.getGroups();
    this.loginService.isAuthenticated();
    this.getUser();
  }

  openSnackBar() {
    this._snackBar.open(this.error, 'Ok');
  }

  getUsers() {
    this.users = [];
    this.loginService
      .getUsers()
      .subscribe( (response:any) => {
        this.clearState();
        this.users = response;
        this.users.forEach(user => {
          this.groups.forEach(group => {
            group.users.map(groupUser => {
              if(groupUser.idUser == user.idUser) {
                user.group = group;
              }
            })
          })
        })
        this.listingUsers = true;
        console.log(this.users);
      }, (error:any) => {
        this.error = error;
      })
  }

  getUser() {
    this.user = new User();
    this.loginService
      .getUser()
      .subscribe( (response:any) => {
        this.clearState();
        this.user = response;
        this.listingUsers = true;
        if(this.user.userType == 'USER') {
          this.router.navigate(['login']);
        }
      }, (error:any) => {
        this.error = error;
      })
  }

  getGroupsOnStart() {
    this.groups = [];
    this.groupService
        .getGroups()
        .subscribe( (response:any) => {
          this.groups = response;
          this.users.forEach(user => {
            this.groups.forEach(group => {
              group.users.map(groupUser => {
                if(groupUser.idUser == user.idUser) {
                  user.group = group;
                }
              })
            })
          })
        }, (error:any) => {
          this.error = error;
        })
  }

  getGroups() {
    this.groups = [];
    this.groupService
        .getGroups()
        .subscribe( (response:any) => {
          this.groups = response;
          this.users.forEach(user => {
            this.groups.forEach(group => {
              group.users.map(groupUser => {
                if(groupUser.idUser == user.idUser) {
                  user.group = group;
                }
              })
            })
          })
          this.clearState();
          this.listingGroups = true;
        }, (error:any) => {
          this.error = error;
        })
  }


  postUser() {
    console.log(this.user);
    this.loginService
      .postUserAuthenticated(this.user)
      .subscribe( (response:any) => {
        this.clearState();
        this.userAdded = true;
      }, (error:any) => {
        this.error = error.error.message;
        this.openSnackBar();
      })
  }


  putUser() {
    console.log(this.user);
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

  postGroup() {
    this.groupService
      .postGroup(this.group)
      .subscribe( (response:any) => {
        this.clearState();
        this.groupAdded = true;
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
          this.error = error;
        })
  }

  editUser(user: User) {
    this.clearState();
    this.editingUser = true;
    this.user = user;
  }

  editGroup(group: Group) {
    this.clearState();
    this.editingGroup = true;
    this.group = group;
  }

  addUser() {
    this.clearState();
    this.user = new User();
    this.addingUser = true;
  }

  addGroup() {
    this.clearState();
    this.group = new Group();
    this.addingGroup = true;
  }

  clearState() {
    this.addingUser = false;
    this.userEdited = false;
    this.editingUser = false;
    this.userAdded = false;
    this.listingUsers = false;
    this.editingGroup = false;
    this.listingGroups = false;
    this.groupAdded = false;
    this.addingGroup = false;
    this.groupEdited = false;
  }

  getUserGroup(user: User) {
    console.log(user);
    if(user.group != undefined) {
      return user.group.name;
    } else {
      return '';
    }
  }

  logOut() {
    sessionStorage.removeItem('oauth2_cs::https://pet-eventos.vercel.app::657287624814-r6u5er6ucbuov555ma58b7rb5qjmin9p.apps.googleusercontent.com');
    sessionStorage.removeItem('oauth2_tr::https://pet-eventos.vercel.app::657287624814-r6u5er6ucbuov555ma58b7rb5qjmin9p.apps.googleusercontent.com::_tr_');
    localStorage.removeItem('oauth2_ss::https://pet-eventos.vercel.app::1::DEFAULT::_ss_');
    localStorage.removeItem('token');
    this.router.navigate(['login']);
  }


}

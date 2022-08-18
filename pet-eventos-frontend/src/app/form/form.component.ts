import { MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from './../login/login.service';
import { User } from './../model/User';
import { EventService } from './../event-home/event.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Event as Events } from '../model/Event';


@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class FormComponent implements OnInit {

  event: Events;
  user: User;
  field: any;
  error: any;
  userFound: any;
  userNotFound: any;
  inscriptionConfirmed: any;
  filteringUser: any;
  inscriptionsClosed: any;

  constructor(private eventService: EventService, private loginService: LoginService, private route: ActivatedRoute, private router: Router, private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.event = new Events();
    this.user = new User();
    this.event.idEvent = this.route.snapshot.params['idEvent'];
    this.getEvent();
  }


  openSnackBar() {
    this._snackBar.open(this.error, 'Ok', {
      duration: 3000
    });
  }

  getEvent() {
    this.eventService
        .getEvent(this.event.idEvent)
        .subscribe( (response:any) => {
          this.event = response;
          if(!this.event.active) {
            this.clearState();
            this.inscriptionsClosed = true;
          } else {
            this.filteringUser = true;
          }

        }, (error:any) => {
          this.error = error.error.message;
          this.openSnackBar();
        })
  }

  filterByRegistration() {
    this.loginService
        .filterByRegistration(this.field)
        .subscribe( (response:any) => {
            this.clearState()
            this.user = response;
            this.userFound = true;
        }, error => {
          this.error = error;
          this.user.registration = this.field;
          this.clearState()
          this.userNotFound = true;
        })
  }

  filterByName() {
    this.loginService
        .filterByName(this.field)
        .subscribe( (response:any) => {
            this.clearState()
            this.user = response;
            this.userFound = true;
        }, error => {
          this.error = error;
          this.user.name = this.field;
          this.clearState()
          this.userNotFound = true;
        })
  }

  find() {
    if(isNaN(this.field)) {
      this.filterByName();
    } else {
      this.filterByRegistration();
    }
  }

  convertDate(selectedDate: Date) {
    const date = new Date(selectedDate);
    var dd = date.getDate();
    var mm = date.getMonth();
    var yyyy = date.getFullYear();
    return dd + '/' + mm + '/' + yyyy;
  }

  confirmInscription() {
    this.eventService
        .addUserToEvent(this.user.idUser, this.event.idEvent)
        .subscribe( (response:any) => {
          this.clearState()
          this.inscriptionConfirmed = true;
        }, (error:any) => {
          this.error = error.error.message;
          this.openSnackBar();
        })
  }

  clearState() {
    this.userNotFound = false;
    this.userFound = false;
    this.inscriptionConfirmed = false;
    this.filteringUser = false;
    this.inscriptionsClosed =false;
  }

  createUserAndConfirmInscription() {
    this.loginService
      .postUser(this.user)
      .subscribe( (response:any) => {
        this.user = response;
        this.confirmInscription();
        this.clearState();
        this.inscriptionConfirmed = true;
      }, (error:any) => {
        console.log(error);
        this.error = error.error.message;
        this.openSnackBar();
      })
  }

  home() {
    this.clearState();
    this.user = new User();
    this.filteringUser = true;
  }

}

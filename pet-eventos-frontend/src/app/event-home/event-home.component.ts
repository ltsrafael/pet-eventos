import { GlobalConstants } from './../common/GlobalConstants';
import { CertificateService } from './../certificate-validation/certificate.service';
import { Attendance } from './../model/Attendance';
import { User } from './../model/User';
import { Section } from './../model/Section';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Router } from '@angular/router';
import { LoginService } from './../login/login.service';
import { EventService } from './event.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Event as Events } from '../model/Event';
import { MatSnackBar } from '@angular/material/snack-bar';



@Component({
  selector: 'app-event-home',
  templateUrl: './event-home.component.html',
  styleUrls: ['./event-home.component.css']
})
export class EventHomeComponent implements OnInit {

  event: Events;
  events: Event[] = [];
  sections: Section[] = [];
  section: Section;
  displayedColumns: string[] =  ['name', 'status', 'view', 'inscription', 'edit', 'delete'];
  error: any;
  dataSource: MatTableDataSource<Event>;
  @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort: MatSort;
  response: any;
  picker: any;
  size: any;
  editingEvent = false;
  listingEvents = false;
  addingEvent = false;
  addingSections = false;
  viewingEvent = false;
  eventCreated = false;
  eventUpdated = false;
  attendanceUpdated = false;
  step = 0;
  minDate : any;
  userAttendances: number[] = [];
  attendance: Attendance;
  eventUsers: User[];
  user: User;
  presences: any;
  processingCertificates: any;
  certificatesGenerated: any;
  userFound: any;
  userNotFound: any;
  inscriptionConfirmed: any;
  filteringUser: any;
  field: any;


  constructor(private eventService: EventService, private loginService: LoginService,
    private certificateService: CertificateService, private router: Router, private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.loginService.isAuthenticated();

    this.getEvents();
    this.event = new Events();
    this.section = new Section();
    this.attendance = new Attendance();
    this.user = new User();
    this.listingEvents = true;
    this.sections.push(this.section);
  }

  openSnackBar() {
    this._snackBar.open(this.error, 'Ok', {
      duration: 3000
    });
  }

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }

  getEvents() {
    this.eventService
        .getEvents()
        .subscribe( (response:any) => {
          this.events = response;
          this.events.reverse();
          this.size = this.events.length;
        }, (error:any) => {
          this.error = error.error.message;
          this.openSnackBar();
        })
  }

  getEvent() {
    this.eventService
        .getEvent(this.event.idEvent)
        .subscribe( (response:any) => {
          this.event = response;
          this.sections = this.event.sections;
          this.sections.forEach(section => {
            this.section = new Section();
            this.section.idSection = section.idSection;
            this.section.start = section.start;
            this.section.end = section.end;
            this.section.attendances = section.attendances;
            this.section.event = section.event;
            this.section.users = section.users;
            this.section.startTime = section.start.getTime().toString();
            this.section.endTime = section.end.getTime().toString();
            section = this.section;
          })
        }, (error:any) => {
          this.error = error.error.message;
          this.openSnackBar();
        })
  }

  getEventById(idEvent: number) {
    this.eventService
        .getEvent(idEvent)
        .subscribe( (response:any) => {
          this.event = response;
          this.sections = this.event.sections;
          this.sections.forEach(section => {
            this.section = new Section();
            this.section.idSection = section.idSection;
            this.section.start = section.start;
            this.section.end = section.end;
            this.section.attendances = section.attendances;
            this.section.event = section.event;
            this.section.users = section.users;
            this.section.startTime = section.start.getTime().toString();
            this.section.endTime = section.end.getTime().toString();
            section = this.section;
          })
        }, (error:any) => {
          this.error = error.error.message;
          this.openSnackBar();
        })
  }

  postEvent() {
    this.event.sections = this.sections;
    this.event.sections.forEach(section => {
      let hourStart = section.startTime[0];
      hourStart = hourStart.concat(section.startTime[1]);
      let minutesStart = section.startTime[2];
      minutesStart = minutesStart.concat(section.startTime[3]);
      section.start.setHours(Number(hourStart));
      section.start.setMinutes(Number(minutesStart));
      section.end.setDate(section.start.getDate());
      section.end.setMonth(section.start.getMonth());
    });

    this.event.sections.forEach(section => {
      let hourEnd = section.endTime[0];
      hourEnd = hourEnd.concat(section.endTime[1]);
      let minutesEnd = section.endTime[2];
      minutesEnd = minutesEnd.concat(section.endTime[3]);
      section.end.setHours(Number(hourEnd));
      section.end.setMinutes(Number(minutesEnd));
      section.end.setDate(section.start.getDate());
      section.end.setMonth(section.start.getMonth());
      section.start = new Date(Date.UTC(section.start.getFullYear(), section.start.getMonth(), section.start.getDate(), section.start.getHours(), section.start.getMinutes()
      , section.start.getSeconds()));
      section.end = new Date(Date.UTC(section.end.getFullYear(), section.end.getMonth(), section.end.getDate(), section.end.getHours(), section.end.getMinutes()
      , section.end.getSeconds()));
    });
    this.eventService
        .postEvent(this.event)
        .subscribe( (response:any) => {
          this.event = response;
          this.size = this.events.length;
          this.clearState();
          this.eventCreated = true;
        }, (error:any) => {
          this.error = error.error.message;
          this.openSnackBar();
        })
  }

  putEvent() {
    this.event.sections = this.sections;

    this.event.sections.forEach(section => {
      let date = section.start.getDate();
      section.end.setDate(date);
      let month = section.start.getMonth();
      section.end.setMonth(month);
      let year = section.start.getFullYear();
      section.end.setFullYear(year);
      let hourEnd = section.endTime[0];
      hourEnd = hourEnd.concat(section.endTime[1]);
      let minutesEnd = section.endTime[2];
      minutesEnd = minutesEnd.concat(section.endTime[3]);
      section.end.setHours(Number(hourEnd));
      section.end.setMinutes(Number(minutesEnd));
      let hourStart = section.startTime[0];
      hourStart = hourStart.concat(section.startTime[1]);
      let minutesStart = section.startTime[2];
      minutesStart = minutesStart.concat(section.startTime[3]);
      section.start.setHours(Number(hourStart));
      section.start.setMinutes(Number(minutesStart));
      section.event = new Events();
      section.event.idEvent = this.event.idEvent;
      section.start = new Date(Date.UTC(section.start.getFullYear(), section.start.getMonth(), section.start.getDate(), section.start.getHours(), section.start.getMinutes()
        , section.start.getSeconds()));
      section.end = new Date(Date.UTC(section.end.getFullYear(), section.end.getMonth(), section.end.getDate(), section.end.getHours(), section.end.getMinutes()
        , section.end.getSeconds()));

    })
    this.eventService
        .putEvent(this.event)
        .subscribe( (response:any) => {
          this.event = response;
          this.size = this.events.length;
          this.clearState();
          this.eventUpdated = true;
        }, (error:any) => {
          this.error = error.error.message;
          this.openSnackBar();
        })
  }

  putEvent2() {
    this.eventService
        .putEvent(this.event)
        .subscribe( (response:any) => {
          this.event = response;
          this.size = this.events.length;
          this.clearState();
          this.listingEvents = true;
          this.getEvents();
        }, (error:any) => {
          this.error = error.error.message;
          this.openSnackBar();
        })
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  editEvent(event: Events) {
    this.clearState();
    this.editingEvent = true;
    this.event = event;
    this.sections = this.event.sections;

    this.event.sections.forEach(section => {
      section.users = [];
    })
    this.event.sections.forEach(section => {
      section.usersPresent = [];
      this.event.users.forEach(user => {
        this.user = new User();
        this.user.idUser = user.idUser;
        this.user.name = user.name;
        section.users.push(this.user);
      })
    } );
    this.event.sections.forEach(section => {
      this.event.attendances.map(attendance => {
        if(section.idSection == attendance.section.idSection) {
          section.usersPresent.push(attendance.user.idUser);
        }
      })
    })
    this.event.sections.forEach(section => {
        section.users.forEach(user => {
        if(section.usersPresent.includes(user.idUser)) {
          user.isPresent = true;
        } else {
          user.isPresent = false;
        }
        })
      });

      this.presences = 0;
      this.event.sections.forEach(section => {
        this.presences += section.usersPresent.length;
      })

      this.sections.forEach(section => {
        this.section = new Section();
        let date1hours = new Date(section.start).getHours();
        let date1minutes = new Date(section.start).getMinutes();
        let date2hours = new Date(section.end).getHours();
        let date2minutes = new Date(section.end).getMinutes();
        this.section.idSection = section.idSection;
        this.section.start = section.start;
        this.section.end = section.end;
        this.section.attendances = section.attendances;
        this.section.event = section.event;
        this.section.users = section.users;
        this.section.startTime = new String();
        this.section.startTime = date1hours + '' + date1minutes;
        if(this.section.startTime.length == 2) {
          var time = this.section.startTime;
          this.section.startTime = '0';
          this.section.startTime = this.section.startTime.concat(time.toString()).concat('0');
        }
        this.section.endTime = new String();
        this.section.endTime = date2hours + '' + date2minutes;
        if(this.section.endTime.length == 2) {
          var time = this.section.endTime;
          this.section.endTime = '0';
          this.section.endTime = this.section.endTime.concat(time.toString()).concat('0');
        }
        if(this.section.startTime.length == 3) {
          this.section.startTime+='0';
        }
        if(this.section.endTime.length == 3) {
          this.section.endTime+='0';
        }
        section.startTime = this.section.startTime;
        section.endTime = this.section.endTime;
      })
      this.event.sections.forEach(section => {
        section.start = new Date(section.start);
        section.end = new Date(section.end);
      })

  }

  listEvents() {
    this.clearState();
    this.getEvents();
    this.listingEvents = true;
  }

  newEvent() {
    this.event = new Events();
    this.sections = [];
    this.clearState();
    this.addingEvent = true;
    this.section = new Section();
    this.sections.push(this.section);
  }

  addSections() {
    this.addingSections = true;
  }

  clearState() {
    this.events = [];
    this.eventCreated = false;
    this.editingEvent = false;
    this.listingEvents = false;
    this.addingEvent = false;
    this.viewingEvent = false;
    this.addingSections = false;
    this.attendanceUpdated = false;
    this.certificatesGenerated = false;
    this.processingCertificates = false;
    this.eventUpdated = false;
    this.step = 0;
    this.userNotFound = false;
    this.userFound = false;
    this.inscriptionConfirmed = false;
    this.filteringUser = false;
  }

  addSection() {
    this.section = new Section();
    this.sections.push(this.section);
  }

  removeSection(arrayIndex: number) {
    this.sections = this.sections.filter((section,index) => arrayIndex != index);
  }

  viewEvent(selectedEvent: Events) {
    this.clearState();

    this.event = selectedEvent;
    let idEvent = selectedEvent.idEvent

    const sectionsList = JSON.parse(JSON.stringify(this.event.sections));
    this.sections = sectionsList;
    this.viewingEvent = true;



    this.event.sections.forEach(section => {
      section.users = [];
    })

    this.event.sections.forEach(section => {
      section.usersPresent = [];
      this.event.users.forEach(user => {
        this.user = new User();
        this.user.idUser = user.idUser;
        this.user.name = user.name;
        section.users.push(this.user);
      })
    } );
    this.event.sections.forEach(section => {
      //section.users = this.event.users;
      this.event.attendances.map(attendance => {
        if(section.idSection == attendance.section.idSection) {
          section.usersPresent.push(attendance.user.idUser);
        }
      })
    })
    this.event.sections.forEach(section => {
        section.users.forEach(user => {
        if(section.usersPresent.includes(user.idUser)) {
          user.isPresent = true;
        } else {
          user.isPresent = false;
        }
        })
      });

      this.presences = 0;
      this.event.sections.forEach(section => {
        this.presences += section.usersPresent.length;
      })
  }

  convertDate(selectedDate: Date) {
    const date = new Date(selectedDate);
    var dd = date.getDate();
    var mm = date.getMonth()+1;
    var yyyy = date.getFullYear();
    return dd + '/' + mm + '/' + yyyy;
  }

  updateAttendances() {
    this.event.attendances = [];
    this.event.sections.forEach(section => {
      section.attendances = []
      section.event = new Events();
      section.event.idEvent = this.event.idEvent;
      section.users.forEach(user => {
        if(user.isPresent) {
          this.attendance = new Attendance();
          this.attendance.user.idUser = user.idUser;
          this.attendance.section.idSection = section.idSection;
          this.attendance.event.idEvent = this.event.idEvent;
          section.attendances.push(this.attendance);
        }
      })

    });
    this.putEvent2();

  }

  finishEvent() {
    this.clearState();
    this.processingCertificates = true;
    this.certificateService
        .finishEventAndGenerateCertificates(this.event.idEvent)
        .subscribe( (response:any) => {
          this.clearState();
          this.certificatesGenerated = true;
        }, (error:any) => {
          this.error = error.error.message;
          this.openSnackBar();
        })

  }

  cancelEvent(event: Events) {
    this.event = event;
    this.event.active = false;
    this.event.cancelled = true;
    this.putEvent2();
  }

  getEventStatus(event: Events) {
    if(event.active) {
      return 'Em Andamento';
    }
    if(event.cancelled) {
      return 'Cancelado';
    }
    if(!event.cancelled && !event.active) {
      return 'Finalizado'
    }
    return "";
  }

  openEventInscription(event: Events) {
    var win = window.open(`${GlobalConstants.eventInscriptionURL}/event/${event.idEvent}`, '_blank');
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
        this.error = error.error.message;
        this.openSnackBar();
      })
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

  addUser() {
    this.clearState();
    this.filteringUser = true;
  }

  home() {
    this.clearState();
    this.user = new User();
    this.filteringUser = true;
  }

}

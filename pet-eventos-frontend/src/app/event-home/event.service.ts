import { GlobalConstants } from './../common/GlobalConstants';
import { Event } from '../model/Event';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  constructor(private http:HttpClient) { }

  public getEvents():Observable<any> {
    return this.http.get(`${GlobalConstants.apiURL}/pet-eventos/event/byGroup`);
  }

  public getEvent(idEvent: number):Observable<any> {
    return this.http.get(`${GlobalConstants.apiURL}/pet-eventos/event/byId?idEvent=${idEvent}`);
  }

  public putEvent(event: Event):Observable<any> {
    return this.http.put(`${GlobalConstants.apiURL}/pet-eventos/event`, event);
  }

  public postEvent(event: Event):Observable<any> {
    return this.http.post(`${GlobalConstants.apiURL}/pet-eventos/event`, event);
  }

  public addUserToEvent(idUser: number, idEvent: number):Observable<any> {
    return this.http.get(`${GlobalConstants.apiURL}/pet-eventos/event/add/user?idUser=${idUser}&idEvent=${idEvent}`);
  }
}

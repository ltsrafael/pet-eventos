import { Group } from './../model/Group';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalConstants } from '../common/GlobalConstants';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  constructor(private http:HttpClient) { }

  public getGroup():Observable<any> {
    return this.http.get(`${GlobalConstants.apiURL}/pet-eventos/group`);
  }

  public getGroups():Observable<any> {
    return this.http.get(`${GlobalConstants.apiURL}/pet-eventos/group/all`);
  }

  public postGroup(group: Group):Observable<any> {
    return this.http.post(`${GlobalConstants.apiURL}/pet-eventos/group`, group);
  }

  public putGroup(group: Group):Observable<any> {
    return this.http.put(`${GlobalConstants.apiURL}/pet-eventos/group`, group);
  }
}

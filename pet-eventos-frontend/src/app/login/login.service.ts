import { User } from './../model/User';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { GlobalConstants } from '../common/GlobalConstants';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  response: any;

  constructor(private http: HttpClient, private router: Router) { }

  authenticate(email: String, password: String):Observable<any> {
    return this.http.post(`${GlobalConstants.apiURL}/pet-eventos/user/authenticated/authenticate`, {email, password});
  }

  googleAuthenticate(accessToken?: string):Observable<any> {
    return this.http.get(`${GlobalConstants.apiURL}/pet-eventos/user/authenticated/authenticate/google?accessToken=` + accessToken);
  }

  getUsers() {
    return this.http.get(`${GlobalConstants.apiURL}/pet-eventos/user/all`);
  }

  getUser() {
    return this.http.get(`${GlobalConstants.apiURL}/pet-eventos/user`);
  }

  postUser(user: User):Observable<any> {
    return this.http.post(`${GlobalConstants.apiURL}/pet-eventos/user/unauthenticated`, user);
  }

  postUserAuthenticated(user: User):Observable<any> {
    return this.http.post(`${GlobalConstants.apiURL}/pet-eventos/user/authenticated`, user);
  }

  putUserAuthenticated(user: User):Observable<any> {
    return this.http.put(`${GlobalConstants.apiURL}/pet-eventos/user/authenticated`, user);
  }

  filterByRegistration(registration: number):Observable<any> {
    return this.http.get(`${GlobalConstants.apiURL}/pet-eventos/user/byRegistration?registration=${registration}`);
  }

  resetPassword(email: String):Observable<any> {
    return this.http.get(`${GlobalConstants.apiURL}/pet-eventos/user/authenticated/reset?email=${email}`);
  }

  filterByName(name: string):Observable<any> {
    const headers= new HttpHeaders()
        .set('name', name);
    return this.http.get(`${GlobalConstants.apiURL}/pet-eventos/user/byName`, {'headers': headers });
  }

  validate():Observable<any> {
    return this.http.get(`${GlobalConstants.apiURL}/pet-eventos/user/authenticated/validate`)
  }

  public getToken() {
    return localStorage.getItem('token');
  }

  isAuthenticated(){
    if(localStorage.getItem('token')){
      this.validate().subscribe( (response:any) => {
        if(response.body == false)  {
          localStorage.removeItem('token');
          this.router.navigate(['login']);
        } else if(response.body == true && this.router.url == '/login'){
          this.router.navigate(['/event']);
        }
      }, (error:any) => {
        localStorage.removeItem('token');
        this.router.navigate(['login']);
      });
    }
    if(localStorage.getItem('token')==null)
    this.router.navigate(['login']);
  }

}

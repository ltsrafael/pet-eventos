import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
  .pipe(
    map(result => result.matches),
    shareReplay()
  );

  constructor(private breakpointObserver: BreakpointObserver, private router: Router) { }

  logOut() {
    sessionStorage.removeItem('oauth2_cs::https://pet-eventos.vercel.app::657287624814-r6u5er6ucbuov555ma58b7rb5qjmin9p.apps.googleusercontent.com');
    sessionStorage.removeItem('oauth2_tr::https://pet-eventos.vercel.app::657287624814-r6u5er6ucbuov555ma58b7rb5qjmin9p.apps.googleusercontent.com::_tr_');
    localStorage.removeItem('oauth2_ss::https://pet-eventos.vercel.app::1::DEFAULT::_ss_');
    localStorage.removeItem('token');
    this.router.navigate(['login']);
  }


}

import { Observable } from 'rxjs';
import { LoginService } from './login/login.service';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorService implements HttpInterceptor{

  constructor(private service: LoginService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let token = this.service.getToken();
        //console.log(token);
        if (token) {
            request = request.clone({
                setHeaders: {
                    'Authorization': `${token}`,
                    'Content-Type': `application/json`
                }
            });
        }
        return next.handle(request);
    }
}

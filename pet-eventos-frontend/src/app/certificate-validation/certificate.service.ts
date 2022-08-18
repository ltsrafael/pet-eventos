import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalConstants } from '../common/GlobalConstants';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private http:HttpClient) { }

  public finishEventAndGenerateCertificates(idEvent: number):Observable<any> {
    return this.http.get(`${GlobalConstants.apiURL}/pet-eventos/certificate/generate?idEvent=${idEvent}`);
  }

  public validateCertificate(certificate: String):Observable<any> {
    return this.http.get(`${GlobalConstants.apiURL}/pet-eventos/certificate/validate?certificate=${certificate}`);
  }

  public downloadCertificate(certificate: String):Observable<any> {
    return this.http.get(`${GlobalConstants.apiURL}/pet-eventos/certificate/download?certificate=${certificate}`, {responseType: 'blob'});
  }
}

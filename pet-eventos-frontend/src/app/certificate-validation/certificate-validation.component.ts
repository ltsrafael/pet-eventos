import { Router } from '@angular/router';
import { CertificateService } from './certificate.service';
import { Certificate } from './../model/Certificate';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-certificate-validation',
  templateUrl: './certificate-validation.component.html',
  styleUrls: ['./certificate-validation.component.css']
})
export class CertificateValidationComponent implements OnInit {


  certificateNumber: any;
  searchingCertificate: boolean;
  certificate: Certificate;
  certificateFound: boolean;
  error: any;

  constructor(private certificateService: CertificateService,  private router: Router) { }

  ngOnInit(): void {
    this.searchingCertificate = true;
  }

  validate() {
      this.certificateService
          .validateCertificate(this.certificateNumber)
          .subscribe( (response:any) => {
            console.log(response);
            this.certificate = response;
            this.clearState();
            this.certificateFound = true;
          }, (error:any) => {
            this.error = 'Certificado não encontrado';
          })
  }

  download() {
    this.certificateService
        .downloadCertificate(this.certificateNumber)
        .subscribe( (response:any) => {
          const file = new Blob([response], {type: 'application/pdf'});
          const fileURL = URL.createObjectURL(file);
          window.open(fileURL, '_blank', 'width=1000, height=800');
          console.log(response);
        }, (error:any) => {
          this.error = 'Certificado não encontrado';
        })
}

  clearState() {
    this.searchingCertificate = false;
    this.certificateFound = false;
  }

  findCertificate() {
    this.clearState();
    this.certificateNumber = null;
    this.certificate = new Certificate();
    this.searchingCertificate = true;
  }

}

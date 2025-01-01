import {Component, OnInit} from '@angular/core';
import {AlertsService} from "../../../services/alerts.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {BillingService} from "../../../services/payment/billing.service";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-card-checkout',
  templateUrl: './card-checkout.component.html',
  styleUrls: ['./card-checkout.component.scss']
})
export class CardCheckoutComponent implements OnInit{

  billingForm = new FormGroup({
    fname: new FormControl('', [Validators.required]),
    lname: new FormControl('', [Validators.required]),
    country: new FormControl('', [Validators.required]),
    address: new FormControl('', [Validators.required]),
    phone: new FormControl('', [Validators.required, Validators.pattern(/^(?:\+?\d{1,3})?(?:0\d{1,3})?\d{7,14}$/)])
  })

  companyId: any;

  constructor(private alertService: AlertsService,
              private cookieService: AuthService,
              private billingService: BillingService,
              private router: Router,
              private route: ActivatedRoute) {
    this.billingForm.get('phone')?.valueChanges.subscribe(value => {
      if (value) {
        const sanitizedValue = value.replace(/\s+/g, ''); // Remove all spaces
        this.billingForm.get('phone')?.setValue(sanitizedValue, { emitEvent: false });
      }
    });
  }

  ngOnInit(): void {
    this.companyId = this.cookieService.organization();
    this.route.queryParams.subscribe(params => {
      if (params['verified'] !== 'true') {
        this.alertService.errorMessage('We detected some suspicious activity. If you face this trouble again and again please contact support!', 'Error');
        setInterval(()=>{
          this.router.navigate(['/cart']);
        }, 5000);
      }
    });
  }

  cancel(){
    this.router.navigate(['/cart']);
  }

  pay(){
    if (this.billingForm.valid){
      this.billingService.savePrePaymentData({
        companyId: this.companyId,
        firstname: this.billingForm.get('fname')?.value,
        lastname: this.billingForm.get('lname')?.value,
        country: this.billingForm.get('country')?.value,
        address: this.billingForm.get('address')?.value,
        phone: this.billingForm.get('phone')?.value,
        payType: 'card',
        status: 'pending'
      }).subscribe((res: any) => {
        if (res) {
          this.router.navigate(['/pay']);
          sessionStorage.setItem('in_payment_progress', 'true');
        } else {
          this.alertService.errorMessage('Something went wrong. Please try again.', 'error');
        }
      }, (err: any) => {
        this.alertService.errorMessage('Something went wrong. Please try again.', 'error');
      })

    }
    else {
      this.alertService.errorMessage('All Fields are required or Phone number is not valid', 'error');
    }
  }

}

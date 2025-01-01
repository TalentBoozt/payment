import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {AuthService} from "../services/auth.service";

@Injectable({
  providedIn: 'root'
})
export class ConfigGuard implements CanActivate {
  constructor(private cookieService:AuthService,
              private route:Router) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
    if(this.cookieService.level() == '0'){
      return true;
    }
    else{
      this.route.navigateByUrl('/403');
      return false;
    }
  }

}

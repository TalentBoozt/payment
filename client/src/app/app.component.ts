import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from "./services/auth.service";
import {ThemeService} from "./services/theme.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, AfterViewInit, OnDestroy {
  title = 'payment request issue -reproduce';

  isTranslator: boolean = false;
  isUiSettings: boolean = false;
  showContacts: boolean = false;

  constructor(public themeService: ThemeService,
              private cookieService: AuthService) {
  }

  ngOnInit() {
    this.cookieService.createOrganizationID('test_21625_id');
    this.themeService.applyTheme();
  }

  ngAfterViewInit() {
    const icons = document.querySelectorAll('.material-icons');
    icons.forEach((icon) => {
      icon.setAttribute('translate', 'no');
    });
  }

  ngOnDestroy() {
    this.removeUnwantedSession()
  }

  toggleTheme() {
    this.themeService.toggleTheme();
  }

  changeColorShading(color: string) {
    this.themeService.changeColorShading(color);
  }

  removeUnwantedSession() {
    sessionStorage.clear();
  }

  clickTranslate() {
    this.isTranslator = !this.isTranslator;
  }

  openUiSettings() {
    this.isUiSettings = !this.isUiSettings;
  }

  toggleContacts() {
    this.showContacts = !this.showContacts;
  }
}

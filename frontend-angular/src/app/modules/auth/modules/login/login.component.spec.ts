import { AuthenticationApiService } from '@auth/services/api/authentication-api.service';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { LoginComponent } from './login.component';
import { Router } from '@angular/router';
import { SnackBarService } from '@root/services/snack-bar.service';
import { TokenService } from '@root/services/token.service';
import { TranslateModule } from '@ngx-translate/core';

describe('LoginComponent', () => {
  let fixture: ComponentFixture<LoginComponent>;
  let component: LoginComponent;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [TranslateModule.forRoot()],
      providers: [
        { provide: Router, useValue: {} },
        { provide: FormBuilder, useValue: {} },
        { provide: AuthenticationApiService, useValue: {} },
        { provide: TokenService, useValue: {} },
        { provide: SnackBarService, useValue: {} },
      ],
    });

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

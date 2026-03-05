import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AbstractControl, FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { of } from 'rxjs/internal/observable/of';

import { Observable } from 'rxjs';
import { Routes } from '@angular/router';
import { LoginComponent } from '../login/login.component';
import { SessionService } from 'src/app/services/session.service';
import { AuthService } from '../../services/auth.service';
import { AuthGuard } from 'src/app/guards/auth.guard';
import { RouterTestingModule } from '@angular/router/testing';

const routes: Routes = [
  { 
    path: 'login',
    //canActivate: [AuthGuard],
    component: LoginComponent   
  }
];

class MyAuthServiceMock {
  register = jest.fn().mockReturnValue(of(null));
}

describe('RegisterComponent fields validation', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
        
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    
    /** Helper – définit la valeur du champ et déclenche la validation */
    const setEmail = (value: string) => {
      const emailControl = component.form?.get('email') as AbstractControl;
      emailControl.setValue(value);
      emailControl.markAsTouched(); // pour que les messages d’erreur s’affichent
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be invalid with a malformed email', () => {
    const invalidEmails = [
      'plainaddress',
      '@missingusername.com',
      'username@.com',
      'username@domain..com',
      'username@domain,com',
    ];
    const emailControl = component.form?.get('email') as AbstractControl;
    invalidEmails.forEach(email => {
      emailControl.setValue(email);
      expect(emailControl.valid).toBeFalsy();
      expect(emailControl.errors?.['email']).toBeTruthy();
    });
  });

  it('should be valid with a correct email', () => {
    const validEmails = [
      'user@example.com',
      'user.name+tag@sub.domain.co',
      'user_name@domain.io',
      'user-name@domain.org',
    ];
    const emailControl = component.form?.get('email') as AbstractControl;
    validEmails.forEach(email => {
      emailControl.setValue(email);
      expect(emailControl.valid).toBeTruthy();
      expect(emailControl.errors).toBeNull();
    });
  });

  it('should mark Email as required', () => {
    const emailControl = component.form?.get('email') as AbstractControl;
    emailControl.setValue('');
    expect(emailControl.valid).toBeFalsy();
    expect(emailControl.hasError('required')).toBeTruthy();
  });

  it('should mark password as required', () => {
      const passwordControl = component.form?.get('password') as AbstractControl;
      passwordControl.setValue('');
      expect(passwordControl.valid).toBeFalsy();
      expect(passwordControl.hasError('required')).toBeTruthy();
  });

  it('should mark firstname as required', () => {
      const firstNameControl = component.form?.get('firstName') as AbstractControl;
      firstNameControl.setValue('');
      expect(firstNameControl.valid).toBeFalsy();
      expect(firstNameControl.hasError('required')).toBeTruthy();
  });

  it('should mark lastname as required', () => {
      const lastNameControl = component.form?.get('lastName') as AbstractControl;
      lastNameControl.setValue('');
      expect(lastNameControl.valid).toBeFalsy();
      expect(lastNameControl.hasError('required')).toBeTruthy();
  });

  it('should mark the field password as invalid if the input is too long', () => {
    const registerControl = component.form?.get('password') as AbstractControl;
    registerControl.setValue('12345678911234567892123456789312345678941');
    expect(registerControl.invalid).toBeTruthy();
    expect(registerControl.errors?.['maxlength']).toBeTruthy();
  });

  it('should mark the field password as invalid if the input is too short', () => {
    const registerControl = component.form?.get('password') as AbstractControl;
    registerControl.setValue('12');
    expect(registerControl.invalid).toBeTruthy();
    expect(registerControl.errors?.['minlength']).toBeTruthy();
  });

});

describe('RegisterComponent submit validation', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  let myAuthServiceMock: MyAuthServiceMock;
  const mockAuthService = {
    sessionInformation: {
      email: '',
      firstName: '',
      lastName: '',
      password: ''
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [SessionService,{provide: AuthService, useClass: MyAuthServiceMock}],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RouterTestingModule.withRoutes(routes)
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    myAuthServiceMock = TestBed.inject(AuthService) as unknown as MyAuthServiceMock;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call this.authService.register() when component.submit() is invoked', () => {
    component.submit();
    expect(myAuthServiceMock.register).toHaveBeenCalledWith(mockAuthService.sessionInformation);
  });

});

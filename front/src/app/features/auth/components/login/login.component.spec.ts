import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AbstractControl, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { LoginComponent } from './login.component';
import { of } from 'rxjs/internal/observable/of';
import { AuthService } from '../../services/auth.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Routes } from '@angular/router';
import { AuthGuard } from 'src/app/guards/auth.guard';

const routes: Routes = [
  {
    path: 'sessions',
    canActivate: [AuthGuard],
    loadChildren: () => import('../../../sessions/sessions.module').then(m => m.SessionsModule)
  },
];

class MyAuthServiceMock {
  login = jest.fn().mockReturnValue(of({
      token: 'FAKE_TOKEN',
      type: 'user',
      id: 1,
      username: 'fg',
      firstName: 'franck',
      lastName: 'Guindeuil',
      admin: false
    } as SessionInformation));
}

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  let myAuthServiceMock: MyAuthServiceMock;
  const mockAuthService = {
    sessionInformation: {
      email: '',
      password: ''
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService,{ provide: AuthService, useClass: MyAuthServiceMock}],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        RouterTestingModule.withRoutes(routes)
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    
    component = fixture.componentInstance;
    myAuthServiceMock = TestBed.inject(AuthService) as unknown as MyAuthServiceMock;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
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

  it('should call this.authService.login() when component.submit() is invoked', () => {
    component.submit();
    expect(myAuthServiceMock.login).toHaveBeenCalledWith(mockAuthService.sessionInformation);
  });

});

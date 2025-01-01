import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: '/pricing', pathMatch: 'full' },
  { path: 'pricing', loadChildren: () => import('./components/prising/prising.module').then(m => m.PrisingModule) },
  { path: 'cart', loadChildren: () => import('./components/payments/cart/cart.module').then(m => m.CartModule) },
  { path: 'card-checkout', loadChildren: () => import('./components/payments/card-checkout/card-checkout.module').then(m => m.CardCheckoutModule) },
  { path: 'bank-checkout', loadChildren: () => import('./components/payments/bank-checkout/bank-checkout.module').then(m => m.BankCheckoutModule) },
  { path: 'pre-order', loadChildren: () => import('./components/shared/pre-order/pre-order.module').then(m => m.PreOrderModule) },
  { path: 'thank-you', loadChildren: () => import('./components/shared/thank-you/thank-you.module').then(m => m.ThankYouModule) },
  { path: 'pay', loadChildren: () => import('./components/payments/stripe-element/stripe-element.module').then(m => m.StripeElementModule) }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

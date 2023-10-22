import Logo from './logo.tsx';
import NavbarItem from './navbar-item.tsx';
import { ReactElement } from 'react';

const Navbar = () => {
  return (
    <div
      id="navbar"
      className="bg-primaryColor flex h-16 w-full items-center">
      <Logo />
      <NavbarItem
        icon={null as never as ReactElement}
        pageName="Delay prediction"
        selected={true}
        route="/"
      />
      <NavbarItem
        icon={null as never as ReactElement}
        pageName="Statistics"
        selected={false}
        route="statistics"
      />
      <NavbarItem
        icon={null as never as ReactElement}
        pageName="Model"
        selected={false}
        route="model"
      />
    </div>
  );
};

export default Navbar;

import Logo from './logo.tsx';
import NavbarItem from './navbar-item.tsx';
import ScheduleIcon from '../assets/icons/schedule.svg';
import BarChartIcon from '../assets/icons/bar-chart.svg';
import SmartToyIcon from '../assets/icons/smart-toy.svg';
import HamburgerMenuIcon from '../assets/icons/menu.svg';
import CloseIcon from '../assets/icons/close.svg';
import { useState } from 'react';
import DrawerMenuItem from './drawer-menu-item.tsx';

const Navbar = () => {
  const [menuIsOpen, setMenuIsOpen] = useState<boolean>(false);

  return (
    <>
      <div
        id="navbar"
        className="flex h-16 w-full items-center justify-start bg-primaryColor">
        <img
          src={HamburgerMenuIcon}
          alt="hamburger-menu"
          className="pl-4 sm:hidden"
          onClick={() => setMenuIsOpen(true)}
        />
        <Logo />
        <div className="hidden h-16 sm:flex">
          <NavbarItem
            icon={ScheduleIcon}
            pageName="Delay prediction"
            route="/"
          />
          <NavbarItem
            icon={BarChartIcon}
            pageName="Statistics"
            route="statistics"
          />
          <NavbarItem
            icon={SmartToyIcon}
            pageName="Model"
            route="model"
          />
        </div>
      </div>
      {menuIsOpen && (
        <div
          id="drawer-menu"
          className="absolute left-0 top-0 z-10 h-full w-64 bg-primaryColor drop-shadow-md">
          <div className="flex h-16 w-full items-center justify-between px-4">
            <Logo />
            <img
              src={CloseIcon}
              alt="close drawer menu"
              onClick={() => setMenuIsOpen(false)}
            />
          </div>
          <DrawerMenuItem
            icon={ScheduleIcon}
            pageName="Delay prediction"
            route="/"
          />
          <DrawerMenuItem
            icon={BarChartIcon}
            pageName="Statistics"
            route="statistics"
          />
          <DrawerMenuItem
            icon={SmartToyIcon}
            pageName="Model"
            route="model"
          />
        </div>
      )}
    </>
  );
};

export default Navbar;

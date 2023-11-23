import Logo from './logo.tsx';
import NavbarItem from './navbar-item.tsx';
import { useState } from 'react';
import DrawerMenuItem from './drawer-menu-item.tsx';
import {
  BarChartIcon,
  MenuIcon,
  ScheduleIcon,
  SmartToyIcon,
  CloseIcon,
} from './icons.tsx';

const Navbar = () => {
  const [menuIsOpen, setMenuIsOpen] = useState<boolean>(false);

  const openMenu = () => setMenuIsOpen(true);
  const closeMenu = () => setMenuIsOpen(false);

  return (
    <>
      <div
        id="navbar"
        className="flex h-16 w-full items-center justify-start bg-primaryColor">
        <MenuIcon
          className="pl-4 text-textColor sm:hidden"
          onClick={openMenu}
        />
        <Logo />
        <div className="hidden h-16 sm:flex">
          <NavbarItem
            icon={<ScheduleIcon className="text-textColor" />}
            pageName="Delay prediction"
            route="/"
          />
          <NavbarItem
            icon={<BarChartIcon className="text-textColor" />}
            pageName="Statistics"
            route="statistics"
          />
          <NavbarItem
            icon={<SmartToyIcon className="text-textColor" />}
            pageName="About"
            route="about"
          />
        </div>
      </div>
      {menuIsOpen && (
        <div
          id="drawer-menu-overlay"
          className="absolute z-10 h-full w-full bg-gray-800 bg-opacity-50">
          <div
            id="drawer-menu"
            className="absolute left-0 top-0 h-full w-64 bg-primaryColor drop-shadow-md">
            <div className="flex h-16 w-full items-center justify-between px-4">
              <Logo />
              <CloseIcon
                onClick={closeMenu}
                className="text-textColor"
              />
            </div>
            <DrawerMenuItem
              icon={<ScheduleIcon className="text-textColor" />}
              pageName="Delay prediction"
              route="/"
              onClick={closeMenu}
            />
            <DrawerMenuItem
              icon={<BarChartIcon className="text-textColor" />}
              pageName="Statistics"
              route="statistics"
              onClick={closeMenu}
            />
            <DrawerMenuItem
              icon={<SmartToyIcon className="text-textColor" />}
              pageName="About"
              route="about"
              onClick={closeMenu}
            />
          </div>
        </div>
      )}
    </>
  );
};

export default Navbar;

import { Tabs, Breadcrumb, Space, Radio } from 'antd';
import React, { useRef, useEffect, useState } from 'react';
import { history, useModel } from '@umijs/max';
import ClassDimensionTable from './ClassDimensionTable';
import ClassMetricTable from './ClassMetricTable';
import PermissionSection from './Permission/PermissionSection';
import TagObjectTable from '../Insights/components/TagObjectTable';
import TermTable from '../components/Term/TermTable';
import OverView from './OverView';
import styles from './style.less';
import { HomeOutlined, FundViewOutlined } from '@ant-design/icons';
import { ISemantic } from '../data';
import SemanticGraphCanvas from '../SemanticGraphCanvas';
import View from '../View';

type Props = {
  isModel: boolean;
  activeKey: string;
  modelList: ISemantic.IModelItem[];
  dataSetList: ISemantic.IDatasetItem[];
  handleModelChange: (model?: ISemantic.IModelItem) => void;
  onBackDomainBtnClick?: () => void;
  onMenuChange?: (menuKey: string) => void;
};
const DomainManagerTab: React.FC<Props> = ({
  isModel,
  activeKey,
  modelList,
  dataSetList,
  handleModelChange,
  onBackDomainBtnClick,
  onMenuChange,
}) => {
  const initState = useRef<boolean>(false);
  const defaultTabKey = 'metric';

  const domainModel = useModel('SemanticModel.domainData');
  const modelModel = useModel('SemanticModel.modelData');

  const { selectDomainId, selectDomainName, selectDomain: domainData } = domainModel;
  const { selectModelId, selectModelName } = modelModel;

  useEffect(() => {
    initState.current = false;
  }, [selectModelId]);

  const [showModelType, setShowModelType] = useState<string>('list');
  const tabItem = [
    {
      label: '数据集管理',
      key: 'overview',
      hidden: !!domainData?.parentId,
      children: <View dataSetList={dataSetList} />,
    },
    {
      label: '模型管理',
      key: 'modelManage',
      children:
        showModelType === 'list' ? (
          <OverView
            modelList={modelList}
            onModelChange={(model) => {
              handleModelChange(model);
            }}
          />
        ) : (
          <div style={{ width: '100%' }} key={selectDomainId}>
            <SemanticGraphCanvas />
          </div>
        ),
    },

    {
      label: '标签对象管理',
      key: 'tagObjectManage',
      hidden: !!!process.env.SHOW_TAG ? true : !!domainData?.parentId,
      children: <TagObjectTable />,
    },
    {
      label: '术语管理',
      key: 'termManage',
      hidden: !!domainData?.parentId,
      children: <TermTable />,
    },
    {
      label: '权限管理',
      key: 'permissonSetting',
      hidden: !!domainData?.parentId,
      children: <PermissionSection permissionTarget={'domain'} />,
    },
  ].filter((item) => {
    if (item.hidden) {
      return false;
    }
    if (domainData?.hasEditPermission) {
      return true;
    }
    return item.key !== 'permissonSetting';
  });

  const isModelItem = [
    {
      label: '指标管理',
      key: 'metric',
      children: (
        <ClassMetricTable
          onEmptyMetricData={() => {
            if (!initState.current) {
              initState.current = true;
              onMenuChange?.('dimenstion');
            }
          }}
        />
      ),
    },
    {
      label: '维度管理',
      key: 'dimenstion',
      children: <ClassDimensionTable />,
    },
    {
      label: '权限管理',
      key: 'permissonSetting',
      children: <PermissionSection permissionTarget={'model'} />,
    },
  ];

  const getActiveKey = () => {
    const key = activeKey || defaultTabKey;
    const tabItems = !isModel ? tabItem : isModelItem;
    const tabItemsKeys = tabItems.map((item) => item.key);
    if (!tabItemsKeys.includes(key)) {
      return tabItemsKeys[0];
    }
    return key;
  };

  return (
    <div>
      <Breadcrumb
        className={styles.breadcrumb}
        separator=""
        items={[
          {
            title: (
              <Space
                onClick={() => {
                  onBackDomainBtnClick?.();
                }}
                style={
                  selectModelName ? { cursor: 'pointer' } : { color: '#296df3', fontWeight: 'bold' }
                }
              >
                <HomeOutlined />
                <span>{selectDomainName}</span>
              </Space>
            ),
          },
          {
            type: 'separator',
            separator: selectModelName ? '/' : '',
          },
          {
            title: selectModelName ? (
              <Space
                onClick={() => {
                  history.push(`/model/${selectDomainId}/${selectModelId}/`);
                }}
                style={{ color: '#296df3' }}
              >
                <FundViewOutlined style={{ position: 'relative', top: '2px' }} />
                <span>{selectModelName}</span>
              </Space>
            ) : undefined,
          },
        ]}
      />
      <Tabs
        className={styles.tab}
        items={!isModel ? tabItem : selectModelId ? isModelItem : []}
        activeKey={getActiveKey()}
        tabBarExtraContent={{
          right:
            getActiveKey() === 'modelManage' ? (
              <Radio.Group
                buttonStyle="solid"
                value={showModelType}
                size="small"
                style={{ marginRight: 25 }}
                onChange={(e) => {
                  const showType = e.target.value;
                  setShowModelType(showType);
                }}
              >
                {showModelType}
                <Radio.Button value="list">列表</Radio.Button>
                <Radio.Button value="canvas">画布</Radio.Button>
              </Radio.Group>
            ) : undefined,
        }}
        size="large"
        onChange={(menuKey: string) => {
          onMenuChange?.(menuKey);
        }}
      />
    </div>
  );
};

export default DomainManagerTab;
